package com.happy.friendogly.club.domain;

import com.happy.friendogly.exception.FriendoglyException;

public enum Status {

    OPEN,
    CLOSED;

    public static Status toStatus(String status){
        try {
            return valueOf(status);
        } catch (IllegalArgumentException e){
            throw new FriendoglyException("존재하지 않는 모임상태 입니다.");
        }
    }
}
