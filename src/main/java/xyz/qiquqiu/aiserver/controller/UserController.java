package xyz.qiquqiu.aiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.qiquqiu.aiserver.common.BaseResult;
import xyz.qiquqiu.aiserver.common.LoginRequest;
import xyz.qiquqiu.aiserver.common.LoginResult;
import xyz.qiquqiu.aiserver.service.IUserService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public BaseResult<Void> register(@RequestBody @Valid LoginRequest req) {
        log.debug("用户注册请求：{}", req);
        boolean isSuccess = userService.save(req);
        return isSuccess ? BaseResult.success() : BaseResult.error("用户名已存在！");
    }

    @PostMapping("/login")
    public BaseResult<LoginResult> login(@RequestBody @Valid LoginRequest req) {
        log.debug("用户登录请求：{}", req);
        return userService.login(req);
    }
}
