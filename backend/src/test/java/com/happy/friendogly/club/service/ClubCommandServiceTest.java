package com.happy.friendogly.club.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.dto.request.SaveClubMemberRequest;
import com.happy.friendogly.club.dto.request.SaveClubRequest;
import com.happy.friendogly.club.dto.response.SaveClubResponse;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class ClubCommandServiceTest extends ClubServiceTest {

    @Autowired
    private ClubCommandService clubCommandService;

    private Member savedMember;
    private Pet savedPet;

    @BeforeEach
    void setMemberAndPet() {
        savedMember = createSavedMember();
        savedPet = createSavedPet(savedMember);
    }

    @DisplayName("모임을 저장한다.")
    @Test
    void save() {
        SaveClubRequest request = new SaveClubRequest(
                "모임 제목",
                "모임 내용",
                "서울특별시",
                "송파구",
                "신천동",
                Set.of(Gender.FEMALE.name(), Gender.FEMALE_NEUTERED.name()),
                Set.of(SizeType.SMALL.name()),
                5,
                List.of(savedPet.getId())
        );
        MockMultipartFile image = new MockMultipartFile(
                "image", "image", MediaType.MULTIPART_FORM_DATA.toString(), "asdf".getBytes());
        SaveClubResponse actual = clubCommandService.save(savedMember.getId(), image, request);

        assertAll(
                () -> assertThat(actual.title()).isEqualTo("모임 제목"),
                () -> assertThat(actual.content()).isEqualTo("모임 내용"),
                () -> assertThat(actual.address().province()).isEqualTo("서울특별시"),
                () -> assertThat(actual.address().city()).isEqualTo("송파구"),
                () -> assertThat(actual.address().village()).isEqualTo("신천동"),
                () -> assertThat(actual.allowedGender()).containsExactlyInAnyOrderElementsOf(
                        Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED))
        );
    }

    @DisplayName("회원이 모임에 참여한다.")
    @Transactional
    @Test
    void joinClub() {
        Club savedClub = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        Member newMember = Member.builder()
                .name("위브")
                .tag("tag123")
                .build();
        Member savedNewMember = memberRepository.save(newMember);
        Pet savedNewMemberPet = createSavedPet(savedNewMember);

        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(savedNewMemberPet.getId()));

        clubCommandService.joinClub(savedClub.getId(), savedNewMember.getId(), request);

        assertThat(savedClub.countClubMember()).isEqualTo(2);
    }

    @DisplayName("이미 참여한 모임에는 참여할 수 없다.")
    @Test
    void joinClub_FailAlreadyParticipating() {
        Club savedClub = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(savedPet.getId()));
        assertThatThrownBy(() -> clubCommandService.joinClub(savedClub.getId(), savedMember.getId(), request))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("이미 참여 중인 모임입니다.");
    }

    @DisplayName("참여 가능한 강아지가 없다면 참여할 수 없다.")
    @Test
    void joinClub_FailCanNotParticipate() {
        Club savedClub = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );
        Member newMember = Member.builder()
                .name("위브")
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

        assertThatThrownBy(() -> clubCommandService.joinClub(savedClub.getId(), savedNewMember.getId(), request))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("모임에 데려갈 수 없는 강아지가 있습니다.");
    }

    @DisplayName("자신이 주인이 아닌 반려견을 모임에 참여시키는 경우 예외가 발생한다.")
    @Test
    void joinClub_FailUnMatchOwner() {
        Club savedClub = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );
        Member newMember = Member.builder()
                .name("위브")
                .tag("tag123")
                .build();
        Member savedNewMember = memberRepository.save(newMember);
        Pet savedNewMemberPet = petRepository.save(
                Pet.builder()
                        .name("땡순이")
                        .description("귀여워요")
                        .member(savedMember)
                        .birthDate(LocalDate.of(2020, 12, 1))
                        .gender(Gender.FEMALE)
                        .sizeType(SizeType.SMALL)
                        .imageUrl("https://image.com")
                        .build()
        );

        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(savedNewMemberPet.getId()));

        assertThatThrownBy(() -> clubCommandService.joinClub(savedClub.getId(), savedNewMember.getId(), request))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("자신의 반려견만 모임에 데려갈 수 있습니다.");
    }

    //영속성 컨텍스트를 프로덕션 코드와 통합시키기 위해 트랜잭셔널 추가
    @DisplayName("참여 중인 회원을 삭제하고, 방장이면 방장을 위임한다.")
    @Transactional
    @Test
    void deleteClubMember() {
        Club savedClub = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        Member newMember = Member.builder()
                .name("위브")
                .tag("tag123")
                .build();

        Member savedNewMember = memberRepository.save(newMember);
        Pet savedNewMemberPet = createSavedPet(savedNewMember);
        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(savedNewMemberPet.getId()));

        clubCommandService.joinClub(savedClub.getId(), savedNewMember.getId(), request);
        clubCommandService.deleteClubMember(savedClub.getId(), savedMember.getId());

        assertAll(
                () -> assertThat(savedClub.countClubMember()).isEqualTo(1),
                () -> assertThat(savedClub.isOwner(savedNewMember)).isTrue()
        );
    }

    @DisplayName("참여 중인 회원 삭제 후 남은 인원이 없으면, 모임을 삭제한다.")
    @Transactional
    @Test
    void deleteClubMember_WhenIsEmptyDelete() {
        Club savedClub = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        clubCommandService.deleteClubMember(savedClub.getId(), savedMember.getId());

        assertThat(clubRepository.findById(savedClub.getId()).isEmpty()).isTrue();
    }
}
