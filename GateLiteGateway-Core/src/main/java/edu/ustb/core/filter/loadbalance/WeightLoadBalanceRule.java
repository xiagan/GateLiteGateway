package edu.ustb.core.filter.loadbalance;

import edu.ustb.core.DynamicConfigManager;
import edu.ustb.common.config.ServiceInstance;
import edu.ustb.common.exception.NotFoundException;
import edu.ustb.core.context.GatewayContext;
import edu.ustb.common.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: ljr-YingWu
 * @date: 2024/5/18 23:45
 * @description:
 */

@Slf4j
public class WeightLoadBalanceRule implements LoadBalanceGatewayRule {

    private final String serviceId;

    public WeightLoadBalanceRule(String serviceId) {
        this.serviceId = serviceId;
    }

    private static ConcurrentHashMap<String, WeightLoadBalanceRule>
            serviceMap = new ConcurrentHashMap<>();

    public static WeightLoadBalanceRule getInstance(String serviceId) {
        WeightLoadBalanceRule loadBalanceRule = serviceMap.get(serviceId);
        if (loadBalanceRule == null) {
            loadBalanceRule = new WeightLoadBalanceRule(serviceId);
            serviceMap.put(serviceId, loadBalanceRule);
        }
        return loadBalanceRule;
    }

    @Override
    public ServiceInstance choose(GatewayContext ctx, boolean gray) {
        String serviceId = ctx.getUniqueId();
        return choose(serviceId, gray);
    }

    @Override
    public ServiceInstance choose(String serviceId, boolean gray) {
        Set<ServiceInstance> serviceInstanceSet =
                DynamicConfigManager.getInstance().getServiceInstanceByUniqueId(serviceId, gray);
        if (serviceInstanceSet.isEmpty()) {
            log.warn("No instance available for: {}", serviceId);
            throw new NotFoundException(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
        }

        // 计算总权重
        int totalWeight = 0;
        for (ServiceInstance instance : serviceInstanceSet) {
            totalWeight += instance.getWeight();
        }

        // 随机选择一个权重值
        int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight);

        // 根据权重选择实例
        for (ServiceInstance instance : serviceInstanceSet) {
            randomWeight -= instance.getWeight();
            if (randomWeight < 0) {
                return instance;
            }
        }

        // 如果未能选择实例，则返回第一个实例
        return serviceInstanceSet.iterator().next();
    }
}