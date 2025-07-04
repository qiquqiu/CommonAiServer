package xyz.qiquqiu.aiserver.service.impl;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.qiquqiu.aiserver.common.BaseResult;
import xyz.qiquqiu.aiserver.common.LoginRequestDTO;
import xyz.qiquqiu.aiserver.common.LoginResultVO;
import xyz.qiquqiu.aiserver.entity.po.User;
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
import java.util.function.Predicate;

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
}
