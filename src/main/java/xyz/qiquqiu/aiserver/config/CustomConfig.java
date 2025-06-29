package xyz.qiquqiu.aiserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class CustomConfig {

    /**
     * 框架封装的bean自定义
     */
    @Bean("customThreadPoolTaskExecutor") // 若不手动指定Bean的id，默认为方法名首字母小写
    public Executor chatTaskExecutor(){
        log.debug("初始化 customThreadPoolTaskExecutor...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 1.核心线程池大小
        executor.setCorePoolSize(5);
        // 2.最大线程池大小
        executor.setMaxPoolSize(10);
        // 3.队列大小
        executor.setQueueCapacity(100);
        // 4.线程名称前缀（自定义）
        executor.setThreadNamePrefix("chat-save-update-");
        // 5.拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
