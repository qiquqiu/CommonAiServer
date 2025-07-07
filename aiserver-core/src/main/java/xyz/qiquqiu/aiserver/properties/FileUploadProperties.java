package xyz.qiquqiu.aiserver.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {
    private String path;
    private String urlPrefix;
}
