package com.woowacourse.friendogly.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        // TODO: 추후 삭제하기!
        return "Hello World!";
    }

    @GetMapping("/log/info")
    public String logInfo() {
        log.info("--- INFO 로그 남기기 테스트 ---");
        return "info";
    }

    @GetMapping("/log/error")
    public String logError() {
        log.error("--- ERROR 로그 남기기 테스트 ---");
        return "error";
    }

    @GetMapping("/log/warn")
    public String logWarn() {
        log.warn("--- WARN 로그 남기기 테스트 ---");
        return "warn";
    }
}
