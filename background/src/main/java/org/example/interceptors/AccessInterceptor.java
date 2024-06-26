package org.example.interceptors;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.utils.JwtUtil;
import org.example.utils.ThreadLocalUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
/**
 * @author TZH
 */
@Component
public class AccessInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        // 令牌验证
        String token = request.getHeader("Authorization");
        /**
         * jwt令牌是无状态的，即后端不会存任何数据，通过解析前端传过来的jwt获取用户信息，jwt有效载荷部分设置了jwt的生命周期
         * 并存储了用户的相关基本信息
         * 这里用Redis来存jwt并验证jwt是为了解决jwt令牌的  主动  失效功能。
         */
        // 验证token
        try {
            // 从redis中获取到相同的token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);
            if(redisToken == null){
                throw new RuntimeException();
            }
            // 解析token获取业务数据
            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 把业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);
            // 放行
            return true;
        } catch (Exception e) {
            // http响应状态码设置为401
            response.setStatus(401);
            // 拦截
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空Threadlocal中的数据
        ThreadLocalUtil.remove();
    }
}