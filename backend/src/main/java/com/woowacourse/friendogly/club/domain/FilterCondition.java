package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;

public enum FilterCondition {

    ALL,
    OPEN,
    ABLE_TO_JOIN;

    public static FilterCondition toFilterCondition(String filterCondition) {
        try {
            return valueOf(filterCondition);
        } catch (IllegalArgumentException e) {
            throw new FriendoglyException("존재하지 않는 FilterCondition 입니다.");
        }
    }
}
