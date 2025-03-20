package edu.ustb.core.filter.loadbalance;

import edu.ustb.common.config.ServiceInstance;
import edu.ustb.core.context.GatewayContext;

/**
 * @author: ljr-YingWu
 * @date: 2023/11/1 9:52
 * LoadBalanceGatewayRule类
 * 负载均衡顶级接口
 */
public interface LoadBalanceGatewayRule {

    /**
     * 通过上下文参数获取服务实例
     * @param ctx
     * @param gray
     * @return
     */
    @Deprecated
    ServiceInstance choose(GatewayContext ctx, boolean gray);

    /**
     * 通过服务ID拿到对应的服务实例
     * @param serviceId
     * @param gray
     * @return
     */
    ServiceInstance choose(String serviceId,boolean gray);

}
