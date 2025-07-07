package xyz.qiquqiu.aiserver.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({MybatisPlusInterceptor.class, BaseMapper.class})
public class MybatisConfig {

    /**
     * MybatisPlusInterceptor：配置 MybatisPlus 的插件拦截器链
     */
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(
            @Autowired(required = false)
            DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor
    ) {
        // 先声明插件拦截器链对象
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // PaginationInnerInterceptor：设置分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(200L);

        // 添加分页拦截器插件到拦截器链
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        return interceptor;
    }
}
