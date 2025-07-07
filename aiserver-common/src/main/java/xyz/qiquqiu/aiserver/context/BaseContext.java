package xyz.qiquqiu.aiserver.context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        Long curId = threadLocal.get();
        log.info("获取到当前访问者 id={}", curId);
        return curId;
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
