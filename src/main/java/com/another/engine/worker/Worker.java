package com.another.engine.worker;

import com.another.engine.model.EngineContext;

public interface Worker {
    public void run(EngineContext engineContext);

    public void rollBack(EngineContext engineContext);
}
