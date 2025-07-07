package xyz.qiquqiu.aiserver.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResultVO {
    private String token;
    private String userId;
    private String username;
}