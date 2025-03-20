package edu.ustb.register.center.api;


import edu.ustb.common.config.ServiceDefinition;
import edu.ustb.common.config.ServiceInstance;

import java.util.Set;


/**
 * @author: ljr-YingWu
 * @date: 2023/10/28 19:57
 * 注册中心的监听器
 * 用来监听注册中心的一些变化
 */
public interface RegisterCenterListener {

    void onChange(ServiceDefinition serviceDefinition,
                  Set<ServiceInstance> serviceInstanceSet);
}
