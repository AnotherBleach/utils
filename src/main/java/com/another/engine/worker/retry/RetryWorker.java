package com.another.engine.worker.retry;

import com.another.annotation.MyWorker;
import com.another.annotation.RetrySuite;
import com.another.constant.SystemConstants;
import com.another.engine.model.EngineContext;
import com.another.engine.worker.Worker;
import com.another.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

@MyWorker
@Slf4j
public class RetryWorker implements Worker {
    @Override
    public void run(EngineContext engineContext) {
        RetrySuite retrySuite = engineContext.get(SystemConstants.retrySuite);
        ProceedingJoinPoint proceedingJoinPoint = engineContext.get(SystemConstants.jointPoint);
        Throwable error=new RuntimeException("业务字段校验失败");
        for (int i = 0; i < retrySuite.maxRetryTimes(); i++) {
            log.info("正在重试第{}次", (i + 1));
            try {
                Object o = proceedingJoinPoint.proceed();
                engineContext.setReturnObj(o);

            } catch (Throwable t) {
                error=t;
                engineContext.setReturnObj(t);
            }

            if (!ValidateUtils.needRetry(retrySuite, engineContext)) {
                log.info("重试第{}次结束", (i + 1));
                return;
            } else {
                log.error("重试第{}次失败{}", (i + 1),error.getMessage());
            }
        }
        log.error("重试{}次最终失败", retrySuite.maxRetryTimes());


    }

    @Override
    public void rollBack(EngineContext engineContext) {

    }
}
