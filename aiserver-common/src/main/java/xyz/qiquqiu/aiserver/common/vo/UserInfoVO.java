package xyz.qiquqiu.aiserver.common.vo;

import lombok.Data;
import xyz.qiquqiu.aiserver.common.po.User;

import java.time.format.DateTimeFormatter;

@Data
public class UserInfoVO {
    private String userId;
    private String username;
    private String registrationDate;

    public static UserInfoVO of(User user) {
        UserInfoVO vo = new UserInfoVO();
        vo.setUserId(user.getId().toString());
        vo.setUsername(user.getUsername());
        vo.setRegistrationDate(user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return vo;
    }
}