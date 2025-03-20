package edu.ustb.core.filter.flow;

import edu.ustb.common.config.Rule;
import edu.ustb.core.context.GatewayContext;
import edu.ustb.core.filter.Filter;
import edu.ustb.core.filter.FilterAspect;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Set;

import static edu.ustb.common.constant.FilterConst.*;

/**
 * @author: ljr-YingWu
 * @date: 2023/11/5 12:09
 * FlowControlFilter类
 * 流量控制过滤器
 * 常见流量控制方法有令牌桶和漏桶算法
 */

@Slf4j
@FilterAspect(id=FLOW_CTL_FILTER_ID,
        name = FLOW_CTL_FILTER_NAME,
        order = FLOW_CTL_FILTER_ORDER)
public class FlowControlFilter implements Filter {
    @Override
    public void doFilter(GatewayContext ctx) throws Exception {
        Rule rule = ctx.getRule();
        if(rule != null){
            //获取流控规则
            Set<Rule.FlowControlConfig> flowControlConfigs = rule.getFlowControlConfigs();
            Iterator iterator = flowControlConfigs.iterator();
            Rule.FlowControlConfig flowControlConfig;
            while (iterator.hasNext()){
                GatewayFlowControlRule flowControlRule = null;
                flowControlConfig = (Rule.FlowControlConfig)iterator.next();
                if(flowControlConfig == null){
                    continue;
                }
                //http-server/ping
                String path = ctx.getRequest().getPath();
                if(flowControlConfig.getType().equalsIgnoreCase(FLOW_CTL_TYPE_PATH)
                        && path.equals(flowControlConfig.getValue())){
                    flowControlRule = FlowControlByPathRule.getInstance(rule.getServiceId(),path);
                }else if(flowControlConfig.getType().equalsIgnoreCase(FLOW_CTL_TYPE_SERVICE)){
                    //TODO 可以自己实现基于服务的流控
                }
                if(flowControlRule != null){
                    //执行流量控制
                    flowControlRule.doFlowControlFilter(flowControlConfig,rule.getServiceId());
                }
            }
        }
    }
}
