package xyz.qiquqiu.aiserver.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.qiquqiu.aiserver.context.BaseContext;
import xyz.qiquqiu.aiserver.properties.JwtProperties;
import xyz.qiquqiu.aiserver.util.JwtUtil;

/**
 * jwt令牌校验拦截器
 * @author lyh
 * @date 2025/6/28 14:33
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    // 注入 jwt 配置
    private final JwtProperties jwtProperties;

    /**
     * 校验jwt
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果是OPTIONS请求，则直接放行，这是CORS预检请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            log.info("OPTIONS请求，直接放行");
            return true;
        }

        // 1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getTokenName());

        // 2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            Long userId = Long.valueOf(claims.get("userId").toString());
            log.info("jwt校验通过,当前用户id:{}", userId);
            // 3、通过
            // BaseContext维护一个ThreadLocal<Long>，即用户id
            BaseContext.setCurrentId(userId);
            return true;
        } catch (Exception ex) {
            // 4、不通过，响应401状态码
            log.info("jwt校验不通过！");
            response.setStatus(401);
            return false;
        }
    }
}
