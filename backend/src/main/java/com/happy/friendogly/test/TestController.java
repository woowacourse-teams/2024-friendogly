package com.happy.friendogly.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/log/info")
    public String info() {
        log.info("INFO LOG!!!");
        return "info..";
    }

    @GetMapping("/log/warn")
    public String warn() {
        log.warn("WARN LOG!!!");
        return "warn...";
    }

    @GetMapping("/log/error")
    public String error() {
        log.error("ERROR LOG!!!");
        return "GG...";
    }
}
