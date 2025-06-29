package xyz.qiquqiu.aiserver.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResult<T> {
    private int code;
    private String message;
    private T data;

    public static <T> BaseResult<T> success() {
        BaseResult<T> result = new BaseResult<>();
        result.code = 200;
        return result;
    }

    // 泛型知识：单独的<T>表示声明该方法为泛型方法，因为T是泛型所以BaseResult<T>也是泛型
    public static <T> BaseResult<T> success(T object) {
        BaseResult<T> result = new BaseResult<>();
        result.data = object;
        result.code = 200;
        return result;
    }

    public static <T> BaseResult<T> error(String msg) {
        BaseResult<T> result = new BaseResult<>();
        result.message = msg;
        result.code = 400;
        return result;
    }

    public static <T> BaseResult<T> error() {
        BaseResult<T> result = new BaseResult<>();
        result.code = 400;
        return result;
    }

    public static <T> BaseResult<T> noLogin() {
        BaseResult<T> result = new BaseResult<>();
        result.message = "用户未登录";
        result.code = 401;
        return result;
    }
}