package com.another.engine.worker.retry;

import com.another.annotation.MyWorker;
import com.another.constant.SystemConstants;
import com.another.engine.model.EngineContext;
import com.another.engine.worker.Worker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

@MyWorker
@Slf4j
public class DoInvokeWorker implements Worker {
    @Override
    public void run(EngineContext engineContext) {
        ProceedingJoinPoint joinPoint = engineContext.get(SystemConstants.jointPoint);
        try {
            log.info("首次调用");
            Object proceed = joinPoint.proceed();
            engineContext.setReturnObj(proceed);
        } catch (Throwable throwable) {
            engineContext.setReturnObj(throwable);
            log.error("调用发生错误{}，准备重试", throwable.getMessage());
        }
    }

    @Override
    public void rollBack(EngineContext engineContext) {

    }
}
