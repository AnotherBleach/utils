package com.another.handler;

import com.another.constant.SystemConstants;
import com.another.engine.WorkerEngine;
import com.another.engine.model.EngineContext;
import com.another.engine.worker.retry.DoInvokeWorker;
import com.another.engine.worker.retry.MetaInfoExtractWorker;
import com.another.engine.worker.retry.RetryCheckWorker;
import com.another.engine.worker.retry.RetryWorker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RetryHandler {

    @Autowired
    WorkerEngine workerEngine;

    @Pointcut("@annotation(com.another.annotation.RetrySuite)")
    public void retry() {
    }


    @Around("retry()")
    public Object handle(ProceedingJoinPoint joinPoint) {
        EngineContext context = new EngineContext();
        context.put(SystemConstants.jointPoint, joinPoint);
        return workerEngine.runWorkers(context,
                MetaInfoExtractWorker.class,
                DoInvokeWorker.class,
                RetryCheckWorker.class,
                RetryWorker.class);
    }

}
