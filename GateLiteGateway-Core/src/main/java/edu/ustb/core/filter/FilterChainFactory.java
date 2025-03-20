package edu.ustb.core.filter;

import edu.ustb.core.context.GatewayContext;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/30 9:48
 * FilterChainFactory接口
 * 过滤器链工厂 用于生成过滤器链
 */
public interface FilterChainFactory {

    /**
     * 构建过滤器链条
     * @param ctx
     * @return
     * @throws Exception
     */
    GatewayFilterChain buildFilterChain(GatewayContext ctx) throws Exception;

    /**
     * 通过过滤器ID获取过滤器
     * @param filterId
     * @return
     * @param <T>
     * @throws Exception
     */
    <T> T getFilterInfo(String filterId) throws Exception;

}
