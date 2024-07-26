package com.woowacourse.friendogly.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        // TODO: 추후 삭제하기!
        return "Hello World!";
    }
}
