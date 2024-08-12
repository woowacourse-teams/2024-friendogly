package com.happy.friendogly.pet.domain;

import com.happy.friendogly.exception.FriendoglyException;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static Set<Gender> toGenders(Set<String> genders) {
        return genders.stream()
                .map(Gender::toGender)
                .collect(Collectors.toSet());
    }
}
