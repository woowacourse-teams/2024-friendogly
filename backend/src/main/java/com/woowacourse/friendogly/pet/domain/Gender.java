package com.woowacourse.friendogly.pet.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;

public enum Gender {

    MALE,
    FEMALE;

    public static Gender toGender(String gender) {
        try {
            return Gender.valueOf(gender);
        } catch (IllegalArgumentException e) {
            throw new FriendoglyException("존재하지 않는 Gender 입니다.");
        }
    }
}
