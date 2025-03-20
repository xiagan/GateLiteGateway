package edu.ustb.core.filter.flow;

import edu.ustb.common.config.Rule;

/**
 * @author: ljr-YingWu
 * @date: 2023/11/5 12:18
 * GatewayFlowControlRule接口
 * 网关流控规则接口
 */
public interface GatewayFlowControlRule {

    /**
     * 执行流控规则过滤器
     * @param flowControlConfig
     * @param serviceId
     */
    void doFlowControlFilter(Rule.FlowControlConfig flowControlConfig, String serviceId);
}
