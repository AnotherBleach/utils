package com.another.engine.model;

import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Data
public class EngineContext {
    ConcurrentHashMap<Object, Object> container = new ConcurrentHashMap<>();
    ConcurrentHashMap<Class, Predicate> retryCheckMap = new ConcurrentHashMap<>();

    Object returnObj;

    boolean stopRunning;

    public void put(Object k, Object v) {
        container.put(k, v);
    }

    public <T> T get(Object k) {
        return (T) container.get(k);
    }

    public Predicate getRetryCheck(Class key) {
        return retryCheckMap.get(key);
    }

    public void putRetryCheck(Class key, Predicate value) {
        retryCheckMap.put(key, value);
    }
}
