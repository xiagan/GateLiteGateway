package edu.ustb.core.filter;

import edu.ustb.core.context.GatewayContext;

/**
 * @author: ljr-YingWu
 * @date: 2023/11/1 9:43
 * Filter接口  过滤器顶层接口
 */
public interface Filter {
    void doFilter(GatewayContext ctx) throws  Exception;

    default int getOrder(){
        FilterAspect annotation = this.getClass().getAnnotation(FilterAspect.class);
        if(annotation != null){
            return annotation.order();
        }
        return Integer.MAX_VALUE;
    };
}
