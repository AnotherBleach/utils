package com.another.test.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonResult<T> {
    int code;
    T obj;
}
