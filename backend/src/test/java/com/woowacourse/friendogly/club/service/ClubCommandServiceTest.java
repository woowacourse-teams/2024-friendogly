package com.woowacourse.friendogly.club.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ClubCommandServiceTest extends ClubServiceTest {

    @Autowired
    private ClubCommandService clubCommandService;

    @DisplayName("모임을 저장한다.")
    @Test
    void save() {
        Member savedMember = memberRepository.save(member);
        Pet savedPet = petRepository.save(pet);
        SaveClubRequest request = new SaveClubRequest(
                "모임 제목",
                "모임 내용",
                "https://clubImage.com",
                "서울특별시 송파구 신정동 잠실 5동",
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL),
                5,
                List.of(1L)
        );
        SaveClubResponse actual = clubCommandService.save(savedMember.getId(), request);

        assertAll(
                () -> assertThat(actual.title()).isEqualTo("모임 제목"),
                () -> assertThat(actual.content()).isEqualTo("모임 내용"),
                () -> assertThat(actual.imageUrl()).isEqualTo("https://clubImage.com"),
                () -> assertThat(actual.address()).isEqualTo("서울특별시 송파구 신정동 잠실 5동"),
                () -> assertThat(actual.allowedGender()).containsExactlyInAnyOrderElementsOf(
                        Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED))
        );
    }
}
