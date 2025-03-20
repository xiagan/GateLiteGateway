package edu.ustb.core.filter.auth;

import edu.ustb.common.enums.ResponseCode;
import edu.ustb.common.exception.ResponseException;
import edu.ustb.core.context.GatewayContext;
import edu.ustb.core.filter.Filter;
import edu.ustb.core.filter.FilterAspect;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static edu.ustb.common.constant.FilterConst.*;

/**
 * @author: ljr-YingWu
 * @date: 2023/11/6 19:43
 * AuthFilter类
 * 用户JWT鉴权过滤器
 */

@Slf4j
@FilterAspect(id= AUTH_FILTER_ID,
        name = AUTH_FILTER_NAME,
        order =AUTH_FILTER_ORDER )
public class AuthFilter implements Filter {
    /**
     * 加密密钥
     */
    private static final String SECRET_KEY = "zhangblossom";

    /**
     * cookie键  从对应的cookie中获取到这个键 存储的就是我们的token信息
     */
    private static final String COOKIE_NAME = "GateLiteGateway-jwt";

    @Override
    public void doFilter(GatewayContext ctx) throws Exception {
        //检查是否需要用户鉴权
        if (ctx.getRule().getFilterConfig(AUTH_FILTER_ID) == null) {
            return;
        }
        //使用auth过滤器，并且没有传递token的时候，有npe问题
        //String token = ctx.getRequest().getCookie(COOKIE_NAME).value();

        // 获取cookie值，如果为null则返回Optional.empty()
        Optional<String> cookieValue = Optional
                .ofNullable(ctx.getRequest().getCookie(COOKIE_NAME))
                .map(cookie -> cookie.value());

        // 检查是否有值，如果有则返回值，否则返回null
        String token = cookieValue.orElse(null);
        if (StringUtils.isBlank(token)) {
            throw new ResponseException(ResponseCode.UNAUTHORIZED);
        }

        try {
            //解析用户id
            long userId = parseUserId(token);
            //把用户id传给下游
            ctx.getRequest().setUserId(userId);
        } catch (Exception e) {
            throw new ResponseException(ResponseCode.UNAUTHORIZED);
        }

    }

    /**
     * 根据token解析用户id
     * @param token
     * @return
     */
    private long parseUserId(String token) {
        Jwt jwt = Jwts.parser().setSigningKey(SECRET_KEY).parse(token);
        return Long.parseLong(((DefaultClaims)jwt.getBody()).getSubject());
    }
}
