package xyz.qiquqiu.aiserver.service;

import xyz.qiquqiu.aiserver.common.BaseResult;
import xyz.qiquqiu.aiserver.common.LoginRequest;
import xyz.qiquqiu.aiserver.common.LoginResult;
import xyz.qiquqiu.aiserver.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lyh
 * @since 2025-06-28
 */
public interface IUserService extends IService<User> {

    boolean save(LoginRequest req);

    BaseResult<LoginResult> login(LoginRequest req);
}
