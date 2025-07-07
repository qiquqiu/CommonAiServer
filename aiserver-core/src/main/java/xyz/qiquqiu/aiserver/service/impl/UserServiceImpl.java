package xyz.qiquqiu.aiserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.qiquqiu.aiserver.common.*;
import xyz.qiquqiu.aiserver.common.dto.ChangePasswordDTO;
import xyz.qiquqiu.aiserver.common.dto.LoginRequestDTO;
import xyz.qiquqiu.aiserver.common.vo.LoginResultVO;
import xyz.qiquqiu.aiserver.common.vo.UserInfoVO;
import xyz.qiquqiu.aiserver.context.BaseContext;
import xyz.qiquqiu.aiserver.common.po.User;
import xyz.qiquqiu.aiserver.mapper.UserMapper;
import xyz.qiquqiu.aiserver.properties.JwtProperties;
import xyz.qiquqiu.aiserver.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.qiquqiu.aiserver.util.JwtUtil;
import xyz.qiquqiu.aiserver.util.MD5Util;

import java.util.HashMap;
import java.util.List;
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
    public boolean saveUser(LoginRequestDTO req) {
        // 新增用户
        String username = req.getUsername();
        String password = MD5Util.encode(req.getPassword());
        User user = new User().setUsername(username).setPassword(password);
        try {
            this.save(user);
        } catch (Exception e) {
            log.error("注册失败！");
            return false;
        }
        log.debug("注册成功！");
        return true;
    }

    // 用户登录
    @Override
    public BaseResult<LoginResultVO> login(LoginRequestDTO req) {
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
        log.debug("登录成功！");
        return BaseResult.success(new LoginResultVO(token, String.valueOf(user.getId()), user.getUsername()));
    }


    // 根据id获取用户信息（不包含密码）
    @Override
    public BaseResult<User> getInfoById(Long id) {
        User user = this.lambdaQuery()
                .eq(User::getId, id)
                .select(field -> !field.getColumn().equals("password"))
                .one();
        return BaseResult.success(user);
    }

    @Override
    public BaseResult<List<User>> getAll() {
        List<User> list = this.lambdaQuery().list();
        return BaseResult.success(list);
    }

    @Override
    public BaseResult<Void> changePassword(ChangePasswordDTO dto) {
        Long userId = BaseContext.getCurrentId();
        User user = this.getById(userId);
        if (user == null) {
            return BaseResult.error("用户不存在！");
        }
        log.debug("用户：{}, {} 修改密码", userId, user.getUsername());
        // 先校验旧密码对不对
        if (!MD5Util.encode(dto.getOldPassword()).equals(user.getPassword())) {
            return BaseResult.error(401, "旧密码错误！");
        }
        // 允许修改密码
        user.setPassword(MD5Util.encode(dto.getNewPassword()));
        this.lambdaUpdate()
                .eq(User::getId, userId)
                .set(User::getPassword, user.getPassword())
                .update();
        return BaseResult.success();
    }

    // 查询当前用户的信息
    @Override
    public BaseResult<UserInfoVO> getMe() {
        Long userId = BaseContext.getCurrentId();
        User user = this.lambdaQuery()
                .eq(User::getId, userId)
                .select(field -> !field.getColumn().equals("password"))
                .one();
        log.debug("获取当前用户信息：{}", user);
        return BaseResult.success(UserInfoVO.of(user));
    }

    // 用户退出登录
    @Override
    public BaseResult<Void> logout() {
        Long userId = BaseContext.getCurrentId();
        log.debug("用户：{} 退出登录", userId);
        BaseContext.removeCurrentId();
        return BaseResult.success();
    }
}
