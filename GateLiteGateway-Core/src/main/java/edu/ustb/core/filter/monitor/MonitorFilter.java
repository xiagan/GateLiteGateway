package edu.ustb.core.filter.monitor;

import edu.ustb.core.context.GatewayContext;
import edu.ustb.core.filter.Filter;
import edu.ustb.core.filter.FilterAspect;
import edu.ustb.common.constant.FilterConst;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/23 19:57
 * Prometheus入口过滤器
 *
 */

@Slf4j
@FilterAspect(id= FilterConst.MONITOR_FILTER_ID,
        name = FilterConst.MONITOR_FILTER_NAME,
        order = FilterConst.MONITOR_FILTER_ORDER)
public class MonitorFilter implements Filter {
    @Override
    public void doFilter(GatewayContext ctx) throws Exception {
        ctx.setTimerSample(Timer.start());
    }
}
