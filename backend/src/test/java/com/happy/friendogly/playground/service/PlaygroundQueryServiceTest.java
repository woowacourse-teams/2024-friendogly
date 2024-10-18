package com.happy.friendogly.playground.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import com.happy.friendogly.playground.dto.response.FindPlaygroundDetailResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundLocationResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundSummaryResponse;
import com.happy.friendogly.playground.dto.response.detail.PlaygroundPetDetail;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PlaygroundQueryServiceTest extends PlaygroundServiceTest {

    @Autowired
    private PlaygroundQueryService playgroundQueryService;

    @DisplayName("놀이터 상세정보를 조회한다.")
    @Test
    void findPlaygroundDetail() {
        // given
        Member member1 = saveMember("member1");
        Member member2 = saveMember("member2");
        savePet(member1);
        savePet(member1);
        savePet(member1);
        savePet(member2);
        Playground playground = savePlayground();
        playgroundMemberRepository.save(
                new PlaygroundMember(
                        playground,
                        member1,
                        "message",
                        true,
                        LocalDateTime.now(),
                        null
                )
        );
        playgroundMemberRepository.save(
                new PlaygroundMember(
                        playground,
                        member2,
                        "message",
                        false,
                        LocalDateTime.now(),
                        null
                )
        );

        // when
        FindPlaygroundDetailResponse response = playgroundQueryService.findDetail(member1.getId(), playground.getId());

        // then
        assertAll(
                () -> assertThat(response.id()).isEqualTo(playground.getId()),
                () -> assertThat(response.totalPetCount()).isEqualTo(4),
                () -> assertThat(response.arrivedPetCount()).isEqualTo(3),
                () -> assertThat(response.playgroundPetDetails().size()).isEqualTo(4)
        );
    }

    @DisplayName("놀이터 상세정보 펫의 정렬조건은 내강아지우선->도착한강아지 순서이다.")
    @Test
    void findSortedDetailPlaygrounds() {
        // given
        Member me = saveMember("김도선");
        Member notArrivedMember = saveMember("이충렬");
        Member arrivedMember = saveMember("이충렬");
        savePet(me);
        savePet(notArrivedMember);
        savePet(arrivedMember);
        Playground playground = savePlayground();
        playgroundMemberRepository.save(new PlaygroundMember(
                playground,
                arrivedMember,
                "도착",
                true,
                LocalDateTime.now(),
                null
        ));
        savePlaygroundMember(playground, me);
        savePlaygroundMember(playground, notArrivedMember);

        // when
        List<PlaygroundPetDetail> detail = playgroundQueryService.findDetail(me.getId(), playground.getId())
                .playgroundPetDetails();

        // when
        assertAll(
                () -> assertThat(detail.get(0).memberId()).isEqualTo(me.getId()),
                () -> assertThat(detail.get(1).memberId()).isEqualTo(arrivedMember.getId()),
                () -> assertThat(detail.get(2).memberId()).isEqualTo(notArrivedMember.getId())
        );
    }

    @DisplayName("놀이터들의 위치를 조회한다.")
    @Test
    void findLocations() {
        // given
        double expectedLatitude = 37.5173316;
        double expectedLongitude = 127.1011661;
        Playground playground = savePlayground(expectedLatitude, expectedLongitude);

        // when
        List<FindPlaygroundLocationResponse> response = playgroundQueryService.findLocations(1L);

        // then
        assertAll(
                () -> assertThat(response.get(0).latitude()).isEqualTo(expectedLatitude),
                () -> assertThat(response.get(0).longitude()).isEqualTo(expectedLongitude)
        );
    }

    @DisplayName("놀이터들의 위치를 조회할 때, 내가 참여했는 지 알 수있다(true)")
    @Test
    void findLocationsWithIsParticipatingTrue() {
        // given
        Member member1 = saveMember("member1");
        savePet(member1);

        Playground playground = savePlayground();
        playgroundMemberRepository.save(
                new PlaygroundMember(
                        playground,
                        member1,
                        "message",
                        false,
                        LocalDateTime.now(),
                        null
                )
        );

        // when
        List<FindPlaygroundLocationResponse> response = playgroundQueryService.findLocations(member1.getId());

        // then
        assertThat(response.get(0).isParticipating()).isEqualTo(true);
    }

    @DisplayName("놀이터들의 위치를 조회할 때, 내가 참여했는 지 알 수있다(false)")
    @Test
    void findLocationsWithIsParticipatingFalse() {
        // given
        Member member1 = saveMember("member1");
        savePet(member1);

        savePlayground();

        // when
        List<FindPlaygroundLocationResponse> response = playgroundQueryService.findLocations(member1.getId());

        // then
        assertThat(response.get(0).isParticipating()).isEqualTo(false);
    }

    @DisplayName("놀이터의 요약정보를 조회한다.")
    @Test
    void findSummary() {
        // given
        Member member = saveMember("김도선");
        Pet pet = savePet(member);
        Playground playground = savePlayground();
        PlaygroundMember playgroundMember = playgroundMemberRepository.save(
                new PlaygroundMember(playground, member, "message", true, LocalDateTime.now(), null)
        );

        // when
        FindPlaygroundSummaryResponse response = playgroundQueryService.findSummary(playground.getId());

        // then
        assertAll(
                () -> assertThat(response.arrivedPetCount()).isEqualTo(1),
                () -> assertThat(response.totalPetCount()).isEqualTo(1),
                () -> assertThat(response.petImageUrls().get(0)).isEqualTo(pet.getImageUrl())
        );
    }

    @DisplayName("놀이터의 요약정보를 조회시 펫이미지는 5개까지만 조회된다.")
    @Test
    void limitPetImageIsFiveWhenFindSummary() {
        // given
        Member member1 = saveMember("김도선1");
        Member member2 = saveMember("김도선2");
        Pet pet = savePet(member1);
        savePet(member1);
        savePet(member1);

        savePet(member2);
        savePet(member2);
        Pet pet6 = savePet(member2);

        Playground playground = savePlayground();
        savePlaygroundMember(playground, member1);
        savePlaygroundMember(playground, member2);

        // when
        FindPlaygroundSummaryResponse response = playgroundQueryService.findSummary(playground.getId());

        // then
        assertThat(response.petImageUrls()).hasSize(5);
    }

    @DisplayName("놀이터의 요약정보를 조회시 도착한 펫우선으로 이미지를 보여준다")
    @Test
    void showArrivedPetImageFirstWhenFindSummary() {
        // given
        Member member1 = saveMember("김도선1");
        Member member2 = saveMember("김도선2");
        Pet pet = savePet(member1);
        savePet(member1);
        savePet(member1);

        Pet arrivedPet = petRepository.save(
                new Pet(
                        member2,
                        "name",
                        "description",
                        LocalDate.of(2023, 10, 10),
                        SizeType.LARGE,
                        Gender.FEMALE,
                        "arrivedPetImage"
                )
        );

        Playground playground = savePlayground();
        savePlaygroundMember(playground, member1);
        saveArrivedPlaygroundMember(playground, member2);

        // when
        FindPlaygroundSummaryResponse response = playgroundQueryService.findSummary(playground.getId());

        // then
        assertThat(response.petImageUrls().get(0)).isEqualTo("arrivedPetImage");
    }

    @DisplayName("놀이터의 요약정보를 조회시 도착한 펫우선으로 보여준후, 참여한 펫 우선으로 보여준다.")
    @Test
    void showParticipatingPetImageSecondWhenFindSummary() {
        // given
        Member member1 = saveMember("김도선");
        Member member2 = saveMember("이충렬");
        Member member3 = saveMember("박예찬");

        savePet(member1);

        Pet arrivedPet = petRepository.save(
                new Pet(
                        member2,
                        "name",
                        "description",
                        LocalDate.of(2023, 10, 10),
                        SizeType.LARGE,
                        Gender.FEMALE,
                        "arrivedPetImage"
                )
        );

        Pet participatingPet = petRepository.save(
                new Pet(
                        member3,
                        "name",
                        "description",
                        LocalDate.of(2023, 10, 10),
                        SizeType.LARGE,
                        Gender.FEMALE,
                        "participatingPetImage"
                )
        );

        Playground playground = savePlayground();
        savePlaygroundMember(playground, member3);
        saveArrivedPlaygroundMember(playground, member2);
        savePlaygroundMember(playground, member1);

        // when
        FindPlaygroundSummaryResponse response = playgroundQueryService.findSummary(playground.getId());

        // then
        assertAll(
                () -> assertThat(response.petImageUrls().get(0)).isEqualTo("arrivedPetImage"),
                () -> assertThat(response.petImageUrls().get(1)).isEqualTo("participatingPetImage")
        );
    }
}
