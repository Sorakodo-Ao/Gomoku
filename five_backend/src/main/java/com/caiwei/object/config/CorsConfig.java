package com.caiwei.object.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                //针对header单独设置，不然无法获取header中的请求信息，前端也无法拿到响应中的                   
                // header信息，OPTIONS请求也会经过拦截器，在进行登录拦截时候要注意特殊处理
                .allowedHeaders("*")
                .exposedHeaders("access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options")
                // 表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
                .maxAge(3600)
                .allowCredentials(true);
    }
}