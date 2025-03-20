package edu.ustb.core.filter;

import edu.ustb.core.context.GatewayContext;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/30 9:48
 * 过滤器链 用于存储实现的过滤器的信息 并且按照顺序进行执行
 */
@Slf4j
public class GatewayFilterChain {

    private List<Filter> filters = new ArrayList<>();


    public GatewayFilterChain addFilter(Filter filter){
        filters.add(filter);
        return this;
    }
    public GatewayFilterChain addFilterList(List<Filter> filter){
        filters.addAll(filter);
        return this;
    }


    /**
     * 执行过滤器处理流程
     * @param ctx
     * @return
     * @throws Exception
     */
    public GatewayContext doFilter(GatewayContext ctx) throws Exception {
        if(filters.isEmpty()){
            return ctx;
        }
        try {
            for(Filter fl: filters){
                fl.doFilter(ctx);
                if (ctx.isTerminated()){
                    break;
                }
            }
        }catch (Exception e){
            log.error("执行过滤器发生异常,异常信息：{}",e.getMessage());
            throw e;
        }
        return ctx;
    }
}
