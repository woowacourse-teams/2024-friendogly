package com.woowacourse.friendogly.pet.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;

public enum Gender {

    MALE,
    FEMALE,
    MALE_NEUTERED,
    FEMALE_NEUTERED;

    public static Gender toGender(String gender) {
        try {
            return valueOf(gender);
        } catch (IllegalArgumentException e) {
            throw new FriendoglyException("존재하지 않는 Gender 입니다.");
        }
    }
}
