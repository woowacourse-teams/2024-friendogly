package com.woowacourse.friendogly.pet.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PetTest {

    private Member member = Member.builder()
            .name("땡이 주인")
            .email("ddang@email.com")
            .build();

    @DisplayName("생성 테스트")
    @Test
    void create() {
        assertThatCode(() ->
                Pet.builder()
                        .member(member)
                        .name("땡이")
                        .description("땡이입니다.")
                        .birthDate(LocalDate.now().minusDays(1L))
                        .sizeType(SizeType.SMALL)
                        .gender(Gender.FEMALE_NEUTERED)
                        .imageUrl("http://www.google.com")
                        .build()
        ).doesNotThrowAnyException();
    }

    @DisplayName("member가 null인 경우 예외가 발생한다.")
    @Test
    void create_Fail_NullMember() {
        assertThatThrownBy(() ->
                Pet.builder()
                        .member(null)
                        .name("땡이")
                        .description("땡이입니다.")
                        .birthDate(LocalDate.now().minusDays(1L))
                        .sizeType(SizeType.SMALL)
                        .gender(Gender.FEMALE_NEUTERED)
                        .imageUrl("http://www.google.com")
                        .build())
                .isExactlyInstanceOf(FriendoglyException.class)
                .hasMessage("member는 null일 수 없습니다.");
    }

    @DisplayName("이름의 길이가 1글자 미만 또는 16글자 이상인 경우 예외가 발생한다.")
    @ValueSource(strings = {"", "1234567890123456"})
    @ParameterizedTest
    void create_Fail_IllegalNameLength(String invalidName) {
        assertThatThrownBy(() ->
                Pet.builder()
                        .member(member)
                        .name(invalidName)
                        .description("땡이입니다.")
                        .birthDate(LocalDate.now().minusDays(1L))
                        .sizeType(SizeType.SMALL)
                        .gender(Gender.FEMALE_NEUTERED)
                        .imageUrl("http://www.google.com")
                        .build())
                .isExactlyInstanceOf(FriendoglyException.class)
                .hasMessage("이름은 1자 이상, 15자 이하여야 합니다.");
    }

    @DisplayName("한 줄 설명의 길이가 1글자 미만 또는 16글자 이상인 경우 예외가 발생한다.")
    @ValueSource(strings = {"", "1234567890123456"})
    @ParameterizedTest
    void create_Fail_IllegalDescriptionLength(String invalidDescription) {
        assertThatThrownBy(() ->
                Pet.builder()
                        .member(member)
                        .name("땡이")
                        .description(invalidDescription)
                        .birthDate(LocalDate.now().minusDays(1L))
                        .sizeType(SizeType.SMALL)
                        .gender(Gender.FEMALE_NEUTERED)
                        .imageUrl("http://www.google.com")
                        .build())
                .isExactlyInstanceOf(FriendoglyException.class)
                .hasMessage("한 줄 설명은 1자 이상, 15자 이하여야 합니다.");
    }

    @DisplayName("생년월일이 현재 날짜보다 미래인 경우 예외가 발생한다.")
    @Test
    void create_Fail_FutureBirthDate() {
        assertThatThrownBy(() ->
                Pet.builder()
                        .member(member)
                        .name("땡이")
                        .description("땡이 입니다.")
                        .birthDate(LocalDate.now().plusDays(1L))
                        .sizeType(SizeType.SMALL)
                        .gender(Gender.FEMALE_NEUTERED)
                        .imageUrl("http://www.google.com")
                        .build())
                .isExactlyInstanceOf(FriendoglyException.class)
                .hasMessage("생년월일은 현재 날짜와 같거나, 이전이어야 합니다.");
    }

    @DisplayName("이미지 URL이 URL 형식이 아닌 경우 예외가 발생한다.")
    @ValueSource(strings = {"google", "https://google", ".com"})
    @ParameterizedTest
    void create_Fail_InvalidUrlFormat(String imageUrl) {
        assertThatThrownBy(() ->
                Pet.builder()
                        .member(member)
                        .name("땡이")
                        .description("땡이 입니다.")
                        .birthDate(LocalDate.now().minusDays(1L))
                        .sizeType(SizeType.SMALL)
                        .gender(Gender.FEMALE_NEUTERED)
                        .imageUrl(imageUrl)
                        .build())
                .isExactlyInstanceOf(FriendoglyException.class)
                .hasMessage("올바른 URL 형식이 아닙니다.");
    }
}
