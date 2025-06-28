package xyz.qiquqiu.aiserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.qiquqiu.aiserver.common.BaseResult;
import xyz.qiquqiu.aiserver.common.LoginRequest;
import xyz.qiquqiu.aiserver.common.LoginResult;
import xyz.qiquqiu.aiserver.entity.po.User;
import xyz.qiquqiu.aiserver.mapper.UserMapper;
import xyz.qiquqiu.aiserver.properties.JwtProperties;
import xyz.qiquqiu.aiserver.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.qiquqiu.aiserver.util.JwtUtil;
import xyz.qiquqiu.aiserver.util.MD5Util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2025-06-28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final JwtProperties jwtProperties;

    // 用户注册
    @Override
    public boolean save(LoginRequest req) {
        // 新增用户
        String username = req.getUsername();
        String password = MD5Util.encode(req.getPassword());
        User user = new User().setUsername(username).setPassword(password);
        try {
            this.save(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 用户登录
    @Override
    public BaseResult<LoginResult> login(LoginRequest req) {
        // 1.判断存在并且校验
        User user = this.lambdaQuery().eq(User::getUsername, req.getUsername()).one();
        if (user == null) {
            return null;
        }
        boolean isMatch = MD5Util.match(req.getPassword(), user.getPassword());
        if (!isMatch) {
            return BaseResult.error("密码错误！");
        }

        long ttl = jwtProperties.getTtl();
        String s = String.valueOf(ttl);
        log.debug("{},{},{}", jwtProperties.getTokenName(), jwtProperties.getSecretKey(), s);

        // 2.存在则生成jwt返回
        Map<String, Object> claims = new HashMap<>(); // jwt的载荷
        claims.put("userId", user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getSecretKey(), ttl, claims);
        return BaseResult.success(new LoginResult(token, String.valueOf(user.getId()), user.getUsername()));
    }
}
