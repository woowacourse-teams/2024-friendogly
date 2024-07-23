package com.woowacourse.friendogly.club.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.domain.ClubMember;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDate;
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
        Member savedMember = createSavedMember();
        Pet savedPet = createSavedPet(savedMember);
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

    @DisplayName("회원이 모임에 참여한다.")
    @Test
    void saveClubMember() {
        Member savedMember = createSavedMember();
        Pet savedPet = createSavedPet(savedMember);
        Club savedClub = createSavedClub(savedMember, savedPet, Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL));

        Member newMember = Member.builder()
                .name("위브")
                .email("wiib@gmail.com")
                .tag("tag123")
                .build();
        Member savedNewMember = memberRepository.save(newMember);

        createSavedPet(savedNewMember);

        assertThatCode(() -> clubCommandService.saveClubMember(savedClub.getId(), savedNewMember.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("이미 참여한 모임에는 참여할 수 없다.")
    @Test
    void saveClubMember_FailAlreadyParticipating() {
        Member savedMember = createSavedMember();
        Pet savedPet = createSavedPet(savedMember);
        Club savedClub = createSavedClub(savedMember, savedPet, Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL));

        Member newMember = Member.builder()
                .name("위브")
                .email("wiib@gmail.com")
                .tag("tag123")
                .build();
        Member savedNewMember = memberRepository.save(newMember);

        clubMemberRepository.save(ClubMember.create(savedClub, newMember));

        assertThatThrownBy(() -> clubCommandService.saveClubMember(savedClub.getId(), savedNewMember.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("이미 참여 중인 모임입니다.");
    }

    @DisplayName("강아지를 등록하지 않는 사용자는 모임에 참여할 수 없다.")
    @Test
    void saveClubMember_FailWithoutPet() {
        Member savedMember = createSavedMember();
        Pet savedPet = createSavedPet(savedMember);
        Club savedClub = createSavedClub(savedMember, savedPet, Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL));
        Member newMember = Member.builder()
                .name("위브")
                .email("wiib@gmail.com")
                .tag("tag123")
                .build();
        Member savedNewMember = memberRepository.save(newMember);

        assertThatThrownBy(() -> clubCommandService.saveClubMember(savedClub.getId(), savedNewMember.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("참여할 수 없는 모임 입니다.");
    }

    @DisplayName("참여 가능한 강아지가 없다면 참여할 수 없다.")
    @Test
    void saveClubMember_FailCanNotParticipate() {
        Member savedMember = createSavedMember();
        Pet savedPet = createSavedPet(savedMember);
        Club savedClub = createSavedClub(savedMember, savedPet, Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL));
        Member newMember = Member.builder()
                .name("위브")
                .email("wiib@gmail.com")
                .tag("tag123")
                .build();
        Member savedNewMember = memberRepository.save(newMember);
        //대형견 수컷이라 참여 불가능
        petRepository.save(
                Pet.builder()
                        .name("스누피")
                        .description("건강한 남자아이에용")
                        .member(savedNewMember)
                        .birthDate(LocalDate.of(2020, 12, 1))
                        .gender(Gender.MALE)
                        .sizeType(SizeType.LARGE)
                        .imageUrl("https://image.com")
                        .build()
        );

        assertThatThrownBy(() -> clubCommandService.saveClubMember(savedClub.getId(), savedNewMember.getId()))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("참여할 수 없는 모임 입니다.");
    }
}
