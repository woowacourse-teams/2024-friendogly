package com.woowacourse.friendogly.club.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Content {

    private static final int MAX_CONTENT_LENGTH = 1000;

    @Lob
    @Column(nullable = false)
    private String content;

    public Content(String content) {
        validate(content);
        this.content =
                content;
    }

    private void validate(String content) {
        if (StringUtils.isBlank(content) || content.length() > MAX_CONTENT_LENGTH) {
            throw new FriendoglyException(String.format("내용은 1글자 이상 %d글자 이하로 작성해주세요.", MAX_CONTENT_LENGTH));
        }
    }
}
