package com.happy.friendogly.pet.domain;

import com.happy.friendogly.exception.FriendoglyException;
import java.util.Set;
import java.util.stream.Collectors;

public enum SizeType {

    SMALL,
    MEDIUM,
    LARGE;

    public static SizeType toSizeType(String sizeType) {
        try {
            return valueOf(sizeType);
        } catch (IllegalArgumentException e) {
            throw new FriendoglyException("존재하지 않는 SizeType 입니다.");
        }
    }

    public static Set<SizeType> toSizeTypes(Set<String> sizeType) {
        return sizeType.stream()
                .map(SizeType::toSizeType)
                .collect(Collectors.toSet());
    }
}
