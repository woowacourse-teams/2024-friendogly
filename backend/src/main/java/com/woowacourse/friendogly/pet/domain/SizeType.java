package com.woowacourse.friendogly.pet.domain;

public enum SizeType {
    SMALL("소형견"),
    MEDIUM("중형견"),
    LARGE("대형견"),
    ;

    private final String value;

    SizeType(String value) {
        this.value = value;
    }

    public static SizeType toSizeType(String sizeType) {
        for (SizeType value : values()) {
            if (value.value.equals(sizeType)) {
                return value;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 SizeType이 입력되었습니다.");
    }

    public String getValue() {
        return value;
    }
}
