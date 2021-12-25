package com.another.engine.worker.retry;

import com.another.annotation.MyWorker;
import com.another.annotation.RetrySuite;
import com.another.constant.SystemConstants;
import com.another.engine.model.EngineContext;
import com.another.engine.worker.Worker;
import com.another.utils.ValidateUtils;
@MyWorker
public class RetryCheckWorker implements Worker {
    @Override
    public void run(EngineContext engineContext) {
        RetrySuite retrySuite = engineContext.get(SystemConstants.retrySuite);


        if (!ValidateUtils.needRetry(retrySuite, engineContext)) {
            engineContext.setStopRunning(true);
        }

    }



    @Override
    public void rollBack(EngineContext engineContext) {

    }
}
