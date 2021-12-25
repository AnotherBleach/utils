package com.another.engine;

import com.another.engine.model.EngineContext;
import com.another.engine.worker.Worker;
import com.another.exception.RetryException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class WorkerEngine implements ApplicationContextAware {


    public <T> T runWorkers(EngineContext context, Class<? extends Worker>... workerClassList) {
        Worker[] workerList = new Worker[workerClassList.length];
        for (int i = 0; i < workerList.length; i++) {
            Worker o = applicationContext.getBean(workerClassList[i]);
            workerList[i] = o;

        }
        return runWorkers(context, workerList);
    }

    public <T> T runWorkers(EngineContext context, Worker... workerList) {

        int i = 0;
        try {
            for (; i < workerList.length; i++) {
                workerList[i].run(context);
                if (context.isStopRunning()) {
                    if (context.getReturnObj() instanceof Throwable)
                        break;
                }
            }

        } catch (Exception e) {
            for (; i >= 0; i--) {
                workerList[i].rollBack(context);
            }
        }
        if (context.getReturnObj() instanceof Throwable) {
            Throwable t = (Throwable) context.getReturnObj();
            throw new RetryException(t.getMessage());
        }

        return (T) context.getReturnObj();
    }

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
