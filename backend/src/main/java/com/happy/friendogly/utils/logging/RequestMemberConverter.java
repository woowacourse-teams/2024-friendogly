package com.happy.friendogly.utils.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class RequestMemberConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String memberId = event.getMDCPropertyMap().get("memberId");
        if (memberId == null || memberId.isBlank()) {
            return "";
        }
        return "[req_member=" + memberId + "] ";
    }
}
