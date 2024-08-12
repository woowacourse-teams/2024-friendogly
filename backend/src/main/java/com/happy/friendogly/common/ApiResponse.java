package com.happy.friendogly.common;

public record ApiResponse<T>(boolean isSuccess, T data) {

    public static <T> ApiResponse<T> ofSuccess(T data) {
        return new ApiResponse(true, data);
    }

    public static <T> ApiResponse<T> ofError(T data) {
        return new ApiResponse(false, data);
    }
}
