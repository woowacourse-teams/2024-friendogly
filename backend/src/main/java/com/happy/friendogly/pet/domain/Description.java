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
public class Description {

    private static final int MIN_DESCRIPTION_LENGTH = 1;
    private static final int MAX_DESCRIPTION_LENGTH = 20;

    @Column(name = "description", nullable = false)
    private String value;

    public Description(String value) {
        validateBlank(value);
        validateDescriptionLength(value);
        this.value = value;
    }

    private void validateBlank(String value) {
        if (StringUtils.isBlank(value)) {
            throw new FriendoglyException("한 줄 설명은 빈 값이나 null일 수 없습니다.");
        }
    }

    private void validateDescriptionLength(String value) {
        if (value.length() < MIN_DESCRIPTION_LENGTH || value.length() > MAX_DESCRIPTION_LENGTH) {
            throw new FriendoglyException(String.format(
                    "한 줄 설명은 %d자 이상, %d자 이하여야 합니다.", MIN_DESCRIPTION_LENGTH, MAX_DESCRIPTION_LENGTH));
        }
    }
}
