package xyz.qiquqiu.aiserver.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResultVO {
    private String token;
    private String userId;
    private String username;
}