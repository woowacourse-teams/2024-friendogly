package com.happy.friendogly.member.domain;

import com.happy.friendogly.exception.FriendoglyException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Email {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    @Column(name = "email", nullable = false)
    private String value;

    public Email(String value) {
        validateBlank(value);
        validateFormat(value);
        this.value = value;
    }

    private void validateBlank(String value) {
        if (StringUtils.isBlank(value)) {
            throw new FriendoglyException("이메일은 빈 값이나 null일 수 없습니다.");
        }
    }

    private void validateFormat(String value) {
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(value).matches()) {
            throw new FriendoglyException("올바른 이메일 형식이 아닙니다.");
        }
    }
}
