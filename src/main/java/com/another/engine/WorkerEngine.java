package com.another.engine;

import com.another.engine.model.EngineContext;
import com.another.engine.worker.Worker;
import com.another.exception.RetryException;

import java.util.concurrent.ConcurrentHashMap;

public class WorkerEngine {

    static ConcurrentHashMap<Class, Worker> cache = new ConcurrentHashMap<>();

    public static <T> T runWorkers(EngineContext context, Class<? extends Worker>... workerClassList) {
        Worker[] workerList = new Worker[workerClassList.length];
        for (int i = 0; i < workerList.length; i++) {
            try {
                Worker o = cache.get(workerClassList[i]);
                if (o == null) {
                    o = workerClassList[i].newInstance();
                    cache.put(workerClassList[i], o);
                }
                workerList[i] = o;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return runWorkers(context, workerList);
    }

    public static <T> T runWorkers(EngineContext context, Worker... workerList) {

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
}
