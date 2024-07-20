package com.woowacourse.friendogly.pet.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUrl {

    private static final Pattern VALID_URL_REGEX =
            Pattern.compile("^(https?:\\/\\/)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}(:[0-9]{1,5})?(\\/.*)?$");

    @Column(name = "image_url")
    private String value;

    public ImageUrl(String value) {
        validateUrlFormat(value);
        this.value = value;
    }

    private void validateUrlFormat(String imageUrl) {
        if (!VALID_URL_REGEX.matcher(imageUrl).matches()) {
            throw new FriendoglyException("올바른 URL 형식이 아닙니다.");
        }
    }
}
