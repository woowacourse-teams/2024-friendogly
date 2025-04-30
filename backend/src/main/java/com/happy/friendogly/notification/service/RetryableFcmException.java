package com.happy.friendogly.notification.service;

public class RetryableFcmException extends RuntimeException {

    public RetryableFcmException(String message, Throwable cause) {
        super(message, cause);
    }
}
