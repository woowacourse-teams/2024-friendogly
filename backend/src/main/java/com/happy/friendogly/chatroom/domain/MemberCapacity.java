package com.happy.friendogly.chatroom.domain;

import com.happy.friendogly.exception.FriendoglyException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberCapacity {

    private static final int MIN_CAPACITY = 1;
    private static final int MAX_CAPACITY = 10;

    @Column(name = "member_capacity", nullable = false)
    private int value;

    public MemberCapacity(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MIN_CAPACITY || value > MAX_CAPACITY) {
            throw new FriendoglyException(String.format(
                    "채팅방의 정원은 %d명 이상 %d명 이하로만 설정 가능합니다.", MIN_CAPACITY, MAX_CAPACITY));
        }
    }
}
