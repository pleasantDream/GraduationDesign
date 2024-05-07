package org.example.config;

import org.example.interceptors.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author TZH
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录和注册相关路径不拦截
        registry.addInterceptor(accessInterceptor).excludePathPatterns("/user/login",
                "/user/register", "/user/emailValidation", "/user/loginByEmail");
    }
}

