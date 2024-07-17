package com.woowacourse.friendogly.member.domain;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    private static final int MAX_NAME_LENGTH = 15;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-z0-9]+@[A-z0-9.-]+\\.[A-z]{2,6}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Builder
    public Member(String name, String email) {
        validateName(name);
        validateEmail(email);
        this.name = name;
        this.email = email;
    }

    private void validateName(String value) {
        if (StringUtils.isBlank(value) || value.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("올바른 이름 형식이 아닙니다.");
        }
    }

    private void validateEmail(String value) {
        if (StringUtils.isBlank(value) || !VALID_EMAIL_ADDRESS_REGEX.matcher(value).matches()) {
            throw new IllegalArgumentException("이메일 형식이 아닙니다.");
        }
    }
}
