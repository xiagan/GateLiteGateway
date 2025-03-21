package edu.ustb.register.center.api;


import edu.ustb.common.config.ServiceDefinition;
import edu.ustb.common.config.ServiceInstance;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/28 19:47
 * 注册中心接口方法
 * 用于提供抽象的注册中心的接口
 *
 */
public interface RegisterCenter {

    /**
     *   初始化
     * @param registerAddress  注册中心地址
     * @param env  要注册到的环境
     */
    void init(String registerAddress, String env);

    /**
     * 注册
     * @param serviceDefinition 服务定义信息
     * @param serviceInstance 服务实例信息
     */
    void register(ServiceDefinition serviceDefinition, ServiceInstance serviceInstance);

    /**
     * 注销
     * @param serviceDefinition
     * @param serviceInstance
     */
    void deregister(ServiceDefinition serviceDefinition, ServiceInstance serviceInstance);

    /**
     * 订阅所有服务变更
     * @param registerCenterListener
     */
    void subscribeAllServices(RegisterCenterListener registerCenterListener);
}
