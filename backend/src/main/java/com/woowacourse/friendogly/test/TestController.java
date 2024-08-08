package com.woowacourse.friendogly.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/log/info")
    public void info() {
        log.info("INFO LOG!!!");
    }

    @GetMapping("/log/warn")
    public void warn() {
        log.warn("WARN LOG!!!");
    }

    @GetMapping("/log/error")
    public void error() {
        log.error("ERROR LOG!!!");
    }
}
