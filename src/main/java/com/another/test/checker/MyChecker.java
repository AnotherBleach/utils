package com.another.test.checker;

import com.another.test.model.CommonResult;

import java.util.function.Predicate;

public class MyChecker implements Predicate<CommonResult<String>> {
    @Override
    public boolean test(CommonResult commonResult) {
        return commonResult.getCode() == -1;
    }
}
