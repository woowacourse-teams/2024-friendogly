package com.happy.friendogly.pet.domain;

import com.happy.friendogly.exception.FriendoglyException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BirthDate {

    @Column(name = "birth_date", nullable = false)
    private LocalDate value;

    public BirthDate(LocalDate value) {
        validateNotNull(value);
        validateBirthDate(value);
        this.value = value;
    }

    private void validateNotNull(LocalDate value) {
        if (value == null) {
            throw new FriendoglyException("생년월일은 필수 입력 값입니다.");
        }
    }

    private void validateBirthDate(LocalDate value) {
        if (value.isAfter(LocalDate.now())) {
            throw new FriendoglyException("생년월일은 현재 날짜와 같거나, 이전이어야 합니다.");
        }
    }
}
