package com.happy.friendogly.club.domain;

import com.happy.friendogly.exception.FriendoglyException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Title {

    private static final int MAX_TITLE_LENGTH = 100;

    @Column(name = "title", nullable = false)
    private String value;

    public Title(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (StringUtils.isBlank(value) || value.length() > MAX_TITLE_LENGTH) {
            throw new FriendoglyException(String.format("제목은 1글자 이상 %d글자 이하로 작성해주세요.", MAX_TITLE_LENGTH));
        }
    }
}
