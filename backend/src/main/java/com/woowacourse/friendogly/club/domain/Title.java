package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title {

    private static final int MAX_TITLE_LENGTH = 100;

    @Column(nullable = false)
    private String title;

    public Title(String title) {
        validate(title);
        this.title = title;
    }

    private void validate(String title) {
        if (StringUtils.isBlank(title) || title.length() > MAX_TITLE_LENGTH) {
            throw new FriendoglyException("제목은 1글자 이상 100글자 이하로 작성해주세요.");
        }
    }
}
