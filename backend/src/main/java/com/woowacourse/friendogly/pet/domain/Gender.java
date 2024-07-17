package com.woowacourse.friendogly.pet.domain;

public enum Gender {
    MALE("수컷"),
    FEMALE("암컷"),
    ;

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public static Gender toGender(String gender) {
        for (Gender value : values()) {
            if (value.value.equals(gender)) {
                return value;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 gender가 입력되었습니다.");
    }

    public String getValue() {
        return value;
    }
}
