package com.woowacourse.friendogly.footprint.domain;

public enum WalkStatus {
    BEFORE,
    ONGOING,
    AFTER;

    public boolean isBefore() {
        return this == BEFORE;
    }

    public boolean isOngoing() {
        return this == ONGOING;
    }

    public boolean isAfter() {
        return this == AFTER;
    }
}
