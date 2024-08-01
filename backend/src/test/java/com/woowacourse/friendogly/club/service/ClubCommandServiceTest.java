package com.woowacourse.friendogly.club.service;

import static com.woowacourse.friendogly.pet.domain.Gender.FEMALE;
import static com.woowacourse.friendogly.pet.domain.Gender.FEMALE_NEUTERED;
import static com.woowacourse.friendogly.pet.domain.Gender.MALE;
import static com.woowacourse.friendogly.pet.domain.Gender.MALE_NEUTERED;
import static com.woowacourse.friendogly.pet.domain.SizeType.LARGE;
import static com.woowacourse.friendogly.pet.domain.SizeType.MEDIUM;
import static com.woowacourse.friendogly.pet.domain.SizeType.SMALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.dto.request.SaveClubMemberRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Pet;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

class ClubCommandServiceTest extends ClubServiceTest {

    @Autowired
    private ClubCommandService clubCommandService;

    @DisplayName("모임을 저장한다.")
    @Test
    void save() {
        // when
        MockMultipartFile image = new MockMultipartFile(
                "image", "image", MULTIPART_FORM_DATA_VALUE, "asdf".getBytes());

        SaveClubRequest request = new SaveClubRequest(
                "모임 제목",
                "모임 내용",
                "서울특별시 송파구 신정동 잠실 5동",
                Set.of(FEMALE, FEMALE_NEUTERED),
                Set.of(SMALL),
                5,
                List.of(pet.getId())
        );

        SaveClubResponse response = clubCommandService.save(member.getId(), image, request);

        // then
        assertAll(
                () -> assertThat(response.title()).isEqualTo("모임 제목"),
                () -> assertThat(response.content()).isEqualTo("모임 내용"),
                () -> assertThat(response.address()).isEqualTo("서울특별시 송파구 신정동 잠실 5동"),
                () -> assertThat(response.allowedGender())
                        .containsExactlyInAnyOrderElementsOf(Set.of(FEMALE, FEMALE_NEUTERED))
        );
    }

    @DisplayName("회원이 모임에 참여한다.")
    @Transactional
    @Test
    void joinClub() {
        // given
        Club club = saveClub(
                member,
                pet,
                Set.of(FEMALE, FEMALE_NEUTERED),
                Set.of(SMALL)
        );

        Member newMember = memberRepository.save(
                Member.builder()
                        .name("위브")
                        .email("wiib@gmail.com")
                        .tag("tag123")
                        .build()
        );

        Pet newPet = savePet(newMember, SMALL, FEMALE);

        // when
        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(newPet.getId()));
        clubCommandService.joinClub(club.getId(), newMember.getId(), request);

        // then
        assertThat(club.countClubMember()).isEqualTo(2);
    }

    @DisplayName("이미 참여한 모임에는 참여할 수 없다.")
    @Test
    void joinClub_FailAlreadyParticipating() {
        // given
        Club club = saveClub(
                member,
                pet,
                Set.of(FEMALE, FEMALE_NEUTERED),
                Set.of(SMALL)
        );

        // when
        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(pet.getId()));

        // then
        assertThatThrownBy(() -> clubCommandService.joinClub(club.getId(), member.getId(), request))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("이미 참여 중인 모임입니다.");
    }

    @DisplayName("참여 가능한 강아지가 없다면 참여할 수 없다.")
    @Test
    void joinClub_FailCanNotParticipate() {
        // given
        Club club = saveClub(
                member,
                pet,
                Set.of(FEMALE, FEMALE_NEUTERED),
                Set.of(SMALL)
        );

        Member newMember = memberRepository.save(Member.builder()
                .name("위브")
                .email("wiib@gmail.com")
                .tag("tag123")
                .build());

        Pet newPet = savePet(newMember, LARGE, MALE); // 대형견 수컷이라 참여 불가능

        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(newPet.getId()));

        // when - then
        assertThatThrownBy(() -> clubCommandService.joinClub(club.getId(), newMember.getId(), request))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("모임에 데려갈 수 없는 강아지가 있습니다.");
    }

    @DisplayName("자신이 주인이 아닌 반려견을 모임에 참여시키는 경우 예외가 발생한다.")
    @Test
    void joinClub_FailUnMatchOwner() {
        // given
        Club club = saveClub(
                member,
                pet,
                Set.of(FEMALE, FEMALE_NEUTERED),
                Set.of(SMALL)
        );

        Member newMember = memberRepository.save(
                Member.builder()
                        .name("위브")
                        .email("wiib@gmail.com")
                        .tag("tag123")
                        .build()
        );

        Pet newPet = savePet(member, SMALL, FEMALE);

        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(newPet.getId()));

        // when - then
        assertThatThrownBy(() -> clubCommandService.joinClub(club.getId(), newMember.getId(), request))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("자신의 반려견만 모임에 데려갈 수 있습니다.");
    }

    @DisplayName("모임에 참여할 때, 정원을 초과해서 참석할 수 없다.")
    @Test
    void joinClub_OverCapacity() {
        // given
        Club club = clubRepository.save(
                Club.create(
                        "모임 제목",
                        "모임 설명",
                        "모임 주소",
                        2,
                        member,
                        Set.of(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED),
                        Set.of(SMALL, MEDIUM, LARGE),
                        "https://image.com",
                        List.of(pet)
                )
        );

        Member member2 = memberRepository.save(new Member("m2", "tag2", "a2@a.com", "https://a2.com"));
        Pet pet2 = savePet(member2, SMALL, MALE);
        SaveClubMemberRequest request2 = new SaveClubMemberRequest(List.of(pet2.getId()));
        clubCommandService.joinClub(club.getId(), member2.getId(), request2);

        // when
        Member member3 = memberRepository.save(new Member("m3", "tag3", "a3@a.com", "https://a3.com"));
        Pet pet3 = savePet(member3, SMALL, MALE);
        SaveClubMemberRequest request3 = new SaveClubMemberRequest(List.of(pet3.getId()));

        // then
        assertThatThrownBy(() -> clubCommandService.joinClub(club.getId(), member3.getId(), request3))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("최대 인원을 초과하여 모임에 참여할 수 없습니다.");
    }

    // 영속성 컨텍스트를 프로덕션 코드와 통합시키기 위해 트랜잭셔널 추가
    @DisplayName("참여 중인 회원을 삭제하고, 방장이면 방장을 위임한다.")
    @Transactional
    @Test
    void deleteClubMember() {
        // given
        Club club = saveClub(
                member,
                pet,
                Set.of(FEMALE, FEMALE_NEUTERED),
                Set.of(SMALL)
        );

        Member newMember = memberRepository.save(
                Member.builder()
                        .name("위브")
                        .email("wiib@gmail.com")
                        .tag("tag123")
                        .build()
        );

        Pet newPet = savePet(newMember, SMALL, FEMALE);
        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(newPet.getId()));

        // when
        clubCommandService.joinClub(club.getId(), newMember.getId(), request);
        clubCommandService.deleteClubMember(club.getId(), member.getId());

        // then
        assertAll(
                () -> assertThat(club.countClubMember()).isEqualTo(1),
                () -> assertThat(club.isOwner(newMember)).isTrue()
        );
    }

    @DisplayName("참여 중인 회원 삭제 후 남은 인원이 없으면, 모임을 삭제한다.")
    @Transactional
    @Test
    void deleteClubMember_WhenIsEmptyDelete() {
        // given
        Club club = saveClub(
                member,
                pet,
                Set.of(FEMALE, FEMALE_NEUTERED),
                Set.of(SMALL)
        );

        // when
        clubCommandService.deleteClubMember(club.getId(), member.getId());

        // then
        assertThat(clubRepository.findById(club.getId())).isEmpty();
    }

    @DisplayName("모임에 참여하면 모임 채팅방에도 자동으로 참여한다.")
    @Transactional
    @Test
    void joinClub_Then_JoinChatRoom() {
        // given
        Club club = saveClub(
                member,
                pet,
                Set.of(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED),
                Set.of(SMALL, MEDIUM, LARGE)
        );

        ChatRoom chatRoom = club.getChatRoom();

        Member newMember = memberRepository.save(
                new Member("새로운멤버", "123", "a@a.com", "https://image.com"));

        Pet newPet = petRepository.save(
                new Pet(newMember, "새로운펫", "설명", LocalDate.now().minusYears(1), SMALL, MALE, "https://image.com"));

        // when
        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(newPet.getId()));
        clubCommandService.joinClub(club.getId(), newMember.getId(), request);

        // then
        assertThat(chatRoom.countMembers()).isEqualTo(2);
    }

    @DisplayName("모임에서 나가면 모임 채팅방에서도 자동으로 나가진다.")
    @Test
    void deleteClubMember_Then_LeaveChatRoom() {
        // given
        Club club = saveClub(
                member,
                pet,
                Set.of(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED),
                Set.of(SMALL, MEDIUM, LARGE)
        );

        ChatRoom chatRoom = club.getChatRoom();

        Member newMember = memberRepository.save(
                new Member("새로운멤버", "123", "a@a.com", "https://image.com"));

        Pet newPet = petRepository.save(
                new Pet(newMember, "새로운펫", "설명", LocalDate.now().minusYears(1), SMALL, MALE, "https://image.com"));

        // when
        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(newPet.getId()));
        clubCommandService.joinClub(club.getId(), newMember.getId(), request);
        clubCommandService.deleteClubMember(club.getId(), newMember.getId());

        // then
        assertThat(chatRoom.countMembers()).isEqualTo(1);
    }

    @DisplayName("모임이 사라지면 채팅방도 사라진다.")
    @Test
    void ownerLeaves_Then_ChatRoomAlsoRemove() {
        // given
        Club club = saveClub(
                member,
                pet,
                Set.of(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED),
                Set.of(SMALL, MEDIUM, LARGE)
        );

        // when
        clubCommandService.deleteClubMember(club.getId(), member.getId());

        // then
        assertThat(chatRoomRepository.count()).isZero();
    }
}
