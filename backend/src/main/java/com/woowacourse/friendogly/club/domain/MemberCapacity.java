package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCapacity {

    private static final int MIN_CAPACITY_SIZE = 1;
    private static final int MAX_CAPACITY_SIZE = 5;

    private int value = 0;

    public MemberCapacity(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MIN_CAPACITY_SIZE || value > MAX_CAPACITY_SIZE) {
            throw new FriendoglyException(String.format(
                    "모집 인원은 %d명 이상 %d명 이하 입니다.",
                    MIN_CAPACITY_SIZE,
                    MAX_CAPACITY_SIZE)
            );
        }
    }
}
