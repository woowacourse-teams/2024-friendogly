package com.happy.friendogly.club.domain;

import com.happy.friendogly.exception.FriendoglyException;

public enum FilterCondition {

    ALL,
    OPEN,
    ABLE_TO_JOIN;

    public static FilterCondition from(String filterCondition) {
        try {
            return valueOf(filterCondition);
        } catch (IllegalArgumentException e) {
            throw new FriendoglyException("존재하지 않는 FilterCondition 입니다.");
        }
    }

    public boolean isOpen() {
        return this == OPEN;
    }

    public boolean isAbleToJoin() {
        return this == ABLE_TO_JOIN;
    }
}
