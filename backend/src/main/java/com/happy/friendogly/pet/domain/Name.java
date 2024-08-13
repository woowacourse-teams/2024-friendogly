package com.happy.friendogly.pet.domain;

import com.happy.friendogly.exception.FriendoglyException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Name {

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 15;

    @Column(name = "name", nullable = false)
    private String value;

    public Name(String value) {
        validateBlank(value);
        validateNameLength(value);
        this.value = value;
    }

    private void validateBlank(String value) {
        if (StringUtils.isBlank(value)) {
            throw new FriendoglyException("이름은 빈 값이나 null일 수 없습니다.");
        }
    }

    private void validateNameLength(String value) {
        if (value.length() < MIN_NAME_LENGTH || value.length() > MAX_NAME_LENGTH) {
            throw new FriendoglyException(String.format(
                    "이름은 %d자 이상, %d자 이하여야 합니다.", MIN_NAME_LENGTH, MAX_NAME_LENGTH));
        }
    }
}
