package com.happy.friendogly.club.domain;

import com.happy.friendogly.exception.FriendoglyException;

public enum Status {

    OPEN,
    CLOSED,
    FULL;

    public static Status toStatus(String status) {
        if (OPEN.name().equals(status) || CLOSED.name().equals(status)) {
            return valueOf(status);
        }
        throw new FriendoglyException("존재하지 않는 모임상태 입니다.");
    }

    public boolean isOpen() {
        return this == Status.OPEN;
    }

    public boolean isFull() {
        return this == Status.FULL;
    }
}
