package com.happy.friendogly.utils.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class RequestUriConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String uri = event.getMDCPropertyMap().get("uri");
        if (uri == null || uri.isBlank()) {
            return "";
        }
        return "[uri=" + uri + "] ";
    }
}
