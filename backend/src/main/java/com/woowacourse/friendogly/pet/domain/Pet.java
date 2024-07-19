package com.woowacourse.friendogly.pet.domain;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet {

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 15;
    private static final int MIN_DESCRIPTION_LENGTH = 1;
    private static final int MAX_DESCRIPTION_LENGTH = 15;
    private static final Pattern VALID_URL_REGEX =
            Pattern.compile("^(https?:\\/\\/)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}(:[0-9]{1,5})?(\\/.*)?$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SizeType sizeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private String imageUrl;

    @Builder
    public Pet(
            Member member,
            String name,
            String description,
            LocalDate birthDate,
            SizeType sizeType,
            Gender gender,
            String imageUrl
    ) {
        validateMember(member);
        validateName(name);
        validateDescription(description);
        validateBirthDate(birthDate);
        validateImageUrl(imageUrl);

        this.member = member;
        this.name = name;
        this.description = description;
        this.birthDate = birthDate;
        this.sizeType = sizeType;
        this.gender = gender;
        this.imageUrl = imageUrl;
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new FriendoglyException("member는 null일 수 없습니다.");
        }
    }

    private void validateName(String name) {
        if (StringUtils.isBlank(name) || name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new FriendoglyException(String.format(
                    "이름은 %d자 이상, %d자 이하여야 합니다.", MIN_NAME_LENGTH, MAX_NAME_LENGTH));
        }
    }

    private void validateDescription(String description) {
        if (StringUtils.isBlank(description)
                || description.length() < MIN_DESCRIPTION_LENGTH
                || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new FriendoglyException(String.format(
                    "한 줄 설명은 %d자 이상, %d자 이하여야 합니다.", MIN_DESCRIPTION_LENGTH, MAX_DESCRIPTION_LENGTH));
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new FriendoglyException("생년월일은 필수 입력 값입니다.");
        }

        if (birthDate.isAfter(LocalDate.now())) {
            throw new FriendoglyException("생년월일은 현재 날짜와 같거나, 이전이어야 합니다.");
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (!VALID_URL_REGEX.matcher(imageUrl).matches()) {
            throw new FriendoglyException("올바른 URL 형식이 아닙니다.");
        }
    }
}
