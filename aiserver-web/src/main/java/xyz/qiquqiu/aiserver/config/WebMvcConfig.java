package xyz.qiquqiu.aiserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.servlet.config.annotation.*;
import xyz.qiquqiu.aiserver.interceptor.JwtTokenInterceptor;
import xyz.qiquqiu.aiserver.properties.FileUploadProperties;
import xyz.qiquqiu.aiserver.properties.GalleryProperties;

import java.util.concurrent.Executor;

/**
 * 配置类，注册web层相关组件
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;
    private final FileUploadProperties fileUploadProperties;
    private final GalleryProperties galleryProperties;
    private final Executor customThreadPoolTaskExecutor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/user/**", "/chat/**", "gallery/**")
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
        // Demo:
        // registry.addResourceHandler("/resources/images/**")
        //         .addResourceLocations("file:D:/dev/uploads/images/");
        registry.addResourceHandler(fileUploadProperties.getUrlPrefix() + "**")
                .addResourceLocations("file:" + fileUploadProperties.getPath());

        registry.addResourceHandler(galleryProperties.getUrlPrefix() + "**")
                .addResourceLocations("file:" + galleryProperties.getPath());
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor((AsyncTaskExecutor) customThreadPoolTaskExecutor);
        // 设置异步请求超时时间（毫秒）当一个异步请求在30秒内没有返回结果时，Spring MVC 会自动终止该请求并返回超时错误
        configurer.setDefaultTimeout(30_000);
    }
}
