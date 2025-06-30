package xyz.qiquqiu.aiserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.qiquqiu.aiserver.interceptor.JwtTokenInterceptor;
import xyz.qiquqiu.aiserver.properties.FileUploadProperties;

/**
 * 配置类，注册web层相关组件
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;
    private final FileUploadProperties fileUploadProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/user/**", "/chat/**")
                .excludePathPatterns("/user/login", "/user/register");
    }

    // 配置跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 匹配所有路径
                .allowedOrigins("*") // 允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许所有请求方法
                .allowedHeaders("*"); // 允许所有请求头
    }

    // 配置静态资源访问路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // registry.addResourceHandler("/resources/images/**")
        //         .addResourceLocations("file:D:/dev/uploads/images/");
        registry.addResourceHandler(fileUploadProperties.getUrlPrefix() + "**")
                .addResourceLocations("file:" + fileUploadProperties.getPath());
    }
}
