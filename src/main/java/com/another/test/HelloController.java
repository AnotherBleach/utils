package com.another.test;

import com.another.annotation.RetrySuite;
import com.another.test.checker.MyChecker;
import com.another.test.model.CommonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class HelloController {
    int x = 1;

    @RetrySuite(expectedException = {ArithmeticException.class, RuntimeException.class})
    @RequestMapping("/test")
    public String test() {
        if (x == 1) {
            x++;
            throw new ArithmeticException("ari");
        }
        if (x == 2) {
            x++;
            throw new RuntimeException("run2");
        }
        if (x == 3) {
            x++;
            throw new RuntimeException("run3");
        }
        x = (x + 1) % 4 + 1;

        return "ok";
    }

    int count = 0;

    @RetrySuite(retryChecker = MyChecker.class, maxRetryTimes = 4)
    @RequestMapping("/testModel")
    public CommonResult<String> testModel() {
        count++;
        if (count % 4 != 0) {

            return CommonResult.<String>builder()
                    .code(-1)
                    .obj("das")
                    .build();

        }
        System.out.println(count);
        return CommonResult.<String>builder().code(0).obj("success").build();
    }
}
