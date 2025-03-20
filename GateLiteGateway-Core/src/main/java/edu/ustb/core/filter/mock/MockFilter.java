package edu.ustb.core.filter.mock;

import edu.ustb.common.config.Rule;
import edu.ustb.common.utils.JSONUtil;
import edu.ustb.core.context.GatewayContext;
import edu.ustb.core.filter.Filter;
import edu.ustb.core.filter.FilterAspect;
import edu.ustb.core.helper.ResponseHelper;
import edu.ustb.core.response.GatewayResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static edu.ustb.common.constant.FilterConst.*;

/**
 * @author: ljr-YingWu
 * @date: 2023/11/9 6:53
 * MockFilter类
 * 前端mock过滤器
 */
@Slf4j
@FilterAspect(id=MOCK_FILTER_ID,
        name = MOCK_FILTER_NAME,
        order = MOCK_FILTER_ORDER)
public class MockFilter implements Filter {
    @Override
    public void doFilter(GatewayContext ctx) throws Exception {
        //如果没有配置mock 直接返回
        Rule.FilterConfig config = ctx.getRule().getFilterConfig(MOCK_FILTER_ID);
        if (config == null) {
            return;
        }
        //解析
        Map<String, String> map = JSONUtil.parse(config.getConfig(), Map.class);
        String value = map.get(ctx.getRequest().getMethod().name() + " " + ctx.getRequest().getPath());
        //不为空说明命中了mock规则
        if (value != null) {
            ctx.setResponse(GatewayResponse.buildGatewayResponse(value));
            ctx.written();
            //数据写回
            ResponseHelper.writeResponse(ctx);
            log.info("mock {} {} {}", ctx.getRequest().getMethod(), ctx.getRequest().getPath(), value);
            ctx.terminated();
        }
    }
}
