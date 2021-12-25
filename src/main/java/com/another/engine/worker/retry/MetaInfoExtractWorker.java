package com.another.engine.worker.retry;

import com.another.annotation.RetrySuite;
import com.another.constant.SystemConstants;
import com.another.engine.model.EngineContext;
import com.another.engine.worker.Worker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 读取当前方法的retry注解信息
 */
public class MetaInfoExtractWorker implements Worker {
    @Override
    public void run(EngineContext engineContext) {
        ProceedingJoinPoint proceedingJoinPoint = engineContext.get(SystemConstants.jointPoint);
        RetrySuite retrySuite = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getDeclaredAnnotation(RetrySuite.class);
        if (retrySuite.maxRetryTimes() < 1) {
            throw new RuntimeException("重试次数必须大于等于1");
        }
        engineContext.put(SystemConstants.retrySuite, retrySuite);
    }

    @Override
    public void rollBack(EngineContext engineContext) {

    }
}
