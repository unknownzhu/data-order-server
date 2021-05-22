/*
package com.ctjsoft.orderserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class AdminCommonConfig implements WebMvcConfigurer {



 */
/*   *//*
*/
/**
     * 跨域支持
     * @param registry
     *//*
*/
/*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600 * 24);
    }*//*


    */
/**
     * 自定义拦截器地址
     * @return
     *//*

    @Bean
    public HandlerInterceptor authInterceptor(){
        AuthInterceptor authInterceptor = new AuthInterceptor() {
            @Override
            public boolean isRepeatSubmit(HttpServletRequest request) {
                return false;
            }
        };
        return authInterceptor;
    }

    */
/**
     * 添加拦截路径
     * @param registry
     *//*

*/
/*    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String url = "/**";
        //添加排除排除URL校验路径
        registry.addInterceptor(authInterceptor()).addPathPatterns(url).excludePathPatterns(
                "/test/exclude/*"
        );
    }*//*




}
*/
