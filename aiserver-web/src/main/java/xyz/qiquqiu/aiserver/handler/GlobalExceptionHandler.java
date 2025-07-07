package xyz.qiquqiu.aiserver.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.qiquqiu.aiserver.common.BaseResult;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * @author lyh
 * @date 2025/6/28 14:30
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理由 @Valid 或 @Validated 注解触发的参数校验失败异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 设置HTTP状态码为400
    public BaseResult<Void> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        if (errorMessage.isEmpty()) {
            // 如果没有写校验失败的message，则默认提示参数不合法
            errorMessage = "请求参数不合法";
        }

        log.error("参数校验失败: {}", errorMessage);

        // 自定义响应体
        return BaseResult.error(errorMessage);
    }

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public BaseResult<Void> exceptionHandler(Exception ex) {
        log.error("异常信息：{}", ex.getMessage());
        String message = ex.getMessage();
        return BaseResult.error(message);
    }
}
