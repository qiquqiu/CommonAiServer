package xyz.qiquqiu.aiserver.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.qiquqiu.aiserver.common.BaseResult;

/**
 * 全局异常处理器
 * @author lyh
 * @date 2025/6/28 14:30
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public BaseResult exceptionHandler(Exception ex) {
        log.error("异常信息：{}", ex.getMessage());
        String message = ex.getMessage();
        return BaseResult.error(message);
    }
}
