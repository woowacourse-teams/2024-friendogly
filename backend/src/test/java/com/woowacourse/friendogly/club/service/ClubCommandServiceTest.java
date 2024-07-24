package com.woowacourse.friendogly.club.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.dto.request.SaveClubMemberRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.club.repository.ClubMemberRepository;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ClubCommandServiceTest extends ClubServiceTest {

    @Autowired
    private ClubCommandService clubCommandService;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private EntityManager em;

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
                List.of(savedPet.getId())
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
        Pet savedNewMemberPet = createSavedPet(savedNewMember);

        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(savedNewMemberPet.getId()));

        assertThatCode(() -> clubCommandService.saveClubMember(savedClub.getId(), savedNewMember.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("이미 참여한 모임에는 참여할 수 없다.")
    @Test
    void saveClubMember_FailAlreadyParticipating() {
        Member savedMember = createSavedMember();
        Pet savedPet = createSavedPet(savedMember);
        Club savedClub = createSavedClub(savedMember, savedPet, Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL));

        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(savedPet.getId()));
        assertThatThrownBy(() -> clubCommandService.saveClubMember(savedClub.getId(), savedMember.getId(), request))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("이미 참여 중인 모임입니다.");
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
        Pet savedNewMemberPet = petRepository.save(
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

        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(savedNewMemberPet.getId()));

        assertThatThrownBy(() -> clubCommandService.saveClubMember(savedClub.getId(), savedNewMember.getId(), request))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("모임에 데려갈 수 없는 강아지가 있습니다.");
    }

    //영속성 컨텍스트를 프로덕션 코드와 통합시키기 위해 트랜잭셔널 추가
    @DisplayName("참여 중인 회원을 삭제하고, 방장이면 방장을 위임한다.")
    @Transactional
    @Test
    void deleteClubMember() {
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
        Pet savedNewMemberPet = createSavedPet(savedNewMember);
        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(savedNewMemberPet.getId()));
        clubCommandService.saveClubMember(savedClub.getId(), savedNewMember.getId(), request);

        clubCommandService.deleteClubMember(savedClub.getId(), savedMember.getId());

        assertAll(
                () -> assertThat(savedClub.countClubMember()).isEqualTo(1),
                () -> assertThat(savedClub.getOwner().getId()).isEqualTo(savedNewMember.getId())
        );
    }

    @DisplayName("참여 중인 회원 삭제 후 남은 인원이 없으면, 제거한다.")
    @Transactional
    @Test
    void deleteClubMember_WhenIsEmptyDelete() {
        Member savedMember = createSavedMember();
        Pet savedPet = createSavedPet(savedMember);
        Club savedClub = createSavedClub(savedMember, savedPet, Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL));

        clubCommandService.deleteClubMember(savedClub.getId(), savedMember.getId());

        assertThat(clubRepository.findById(savedClub.getId()).isEmpty()).isTrue();
    }
}