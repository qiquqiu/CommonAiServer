package xyz.qiquqiu.aiserver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.qiquqiu.aiserver.common.BaseResult;
import xyz.qiquqiu.aiserver.common.LoginRequestDTO;
import xyz.qiquqiu.aiserver.common.LoginResultVO;
import xyz.qiquqiu.aiserver.entity.po.User;
import xyz.qiquqiu.aiserver.service.IUserService;

import java.util.List;

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

    @GetMapping("/{id}")
    public BaseResult<User> getById(@PathVariable Long id) {
        log.debug("获取用户信息：id={}", id);
        return userService.getInfoById(id);
    }

    @GetMapping("/all")
    public BaseResult<List<User>> getAll() {
        return userService.getAll();
    }
}
