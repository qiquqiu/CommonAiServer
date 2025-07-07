package xyz.qiquqiu.aiserver.service;

import jakarta.validation.Valid;
import xyz.qiquqiu.aiserver.common.*;
import xyz.qiquqiu.aiserver.common.dto.ChangePasswordDTO;
import xyz.qiquqiu.aiserver.common.dto.LoginRequestDTO;
import xyz.qiquqiu.aiserver.common.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.qiquqiu.aiserver.common.vo.LoginResultVO;
import xyz.qiquqiu.aiserver.common.vo.UserInfoVO;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lyh
 * @since 2025-06-28
 */
public interface IUserService extends IService<User> {

    boolean saveUser(LoginRequestDTO req);

    BaseResult<LoginResultVO> login(LoginRequestDTO req);

    BaseResult<User> getInfoById(Long id);

    BaseResult<List<User>> getAll();

    BaseResult<Void> changePassword(@Valid ChangePasswordDTO dto);

    BaseResult<UserInfoVO> getMe();

    BaseResult<Void> logout();
}
