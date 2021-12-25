package com.another.utils;

import com.another.annotation.RetrySuite;
import com.another.constant.SystemConstants;
import com.another.engine.model.EngineContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class ValidateUtils {
    @SneakyThrows
    public static boolean needRetry(RetrySuite retrySuite, EngineContext engineContext) {
        // 如果失败了，需要重试
        if (engineContext.getReturnObj() instanceof Throwable) {
            Class<?>[] cls = retrySuite.expectedException();
            Class s = engineContext.getReturnObj().getClass();
            for (int i = 0; i < cls.length; i++) {
                if (s == cls[i]) return true;
            }
            List<String> name = Arrays.stream(cls).map(Class::getName).collect(Collectors.toList());
            log.info("当前异常{}，不在用户指定的异常中{}", s.getName(), name);
            return false;
        } else {
            // 如果调用成功,且用户没指定自定义检查器，就成功
            if (retrySuite.retryChecker() == Predicate.class) {
                return false;
            } else {
                Predicate predicate = engineContext.getRetryCheck(retrySuite.retryChecker());

                if (predicate == null) {
                    predicate = retrySuite.retryChecker().newInstance();
                    engineContext.putRetryCheck(retrySuite.retryChecker(), predicate);
                }


                if (predicate.test(engineContext.getReturnObj())) {
                    log.info("未通过用户自定义检查");
                    return true;
                }
                return false;
            }
        }
    }
}
