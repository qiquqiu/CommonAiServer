package xyz.qiquqiu.aiserver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.qiquqiu.aiserver.common.*;
import xyz.qiquqiu.aiserver.common.dto.ChangePasswordDTO;
import xyz.qiquqiu.aiserver.common.dto.LoginRequestDTO;
import xyz.qiquqiu.aiserver.common.vo.LoginResultVO;
import xyz.qiquqiu.aiserver.common.vo.UserInfoVO;
import xyz.qiquqiu.aiserver.service.IUserService;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public BaseResult<Void> register(@RequestBody @Valid LoginRequestDTO req) {
        log.debug("用户注册请求：{}", req);
        boolean isSuccess = userService.saveUser(req);
        return isSuccess ? BaseResult.success() : BaseResult.error("用户名已存在！");
    }

    @PostMapping("/login")
    public BaseResult<LoginResultVO> login(@RequestBody @Valid LoginRequestDTO req) {
        log.debug("用户登录请求：{}", req);
        return userService.login(req);
    }

//    @GetMapping("/{id}")
//    public BaseResult<User> getById(@PathVariable Long id) {
//        log.debug("获取用户信息：id={}", id);
//        return userService.getInfoById(id);
//    }

    @GetMapping("/me")
    public BaseResult<UserInfoVO> getMe() {
        return userService.getMe();
    }

    @PostMapping("/logout")
    public BaseResult<Void> logout() {
        return userService.logout();
    }

//    @GetMapping("/all")
//    public BaseResult<List<User>> getAll() {
//        return userService.getAll();
//    }

    // 修改密码
    @PutMapping("/me/password")
    public BaseResult<Void> changePassword(@RequestBody @Valid ChangePasswordDTO dto) {
        return userService.changePassword(dto);
    }
}
