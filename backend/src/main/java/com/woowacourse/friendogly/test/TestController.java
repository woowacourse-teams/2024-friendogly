package com.woowacourse.friendogly.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/log/info")
    public String info() {
        log.info("INFO LOG!!!");
        return "잘 됩니다.";
    }

    @GetMapping("/log/warn")
    public String warn() {
        log.warn("WARN LOG!!!");
        return "그쪽이 잘못했어요.";
    }

    @GetMapping("/log/error")
    public String error() {
        log.error("ERROR LOG!!!");
        return "서버가 잘못했어요.";
    }
}
