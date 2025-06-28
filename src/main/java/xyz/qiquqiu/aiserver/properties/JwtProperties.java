package xyz.qiquqiu.aiserver.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "aiserver.jwt")
public class JwtProperties {
    /**
     * jwt加密的密钥
     */
    private String secretKey;
    /**
     * 过期时间（单位：毫秒）
     */
    private long ttl;
    /**
     * jwt令牌的请求头名称，默认authorization
     */
    private String tokenName;
}
