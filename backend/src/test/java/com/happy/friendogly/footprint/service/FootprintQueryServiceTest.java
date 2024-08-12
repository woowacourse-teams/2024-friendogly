package com.happy.friendogly.footprint.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.footprint.domain.Footprint;
import com.happy.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.happy.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.happy.friendogly.footprint.dto.response.FindMyLatestFootprintTimeAndPetExistenceResponse;
import com.happy.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.happy.friendogly.footprint.dto.response.FindOneFootprintResponse;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.notification.domain.DeviceToken;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// TODO: Member, Dog 테스트 픽스처 생성
class FootprintQueryServiceTest extends FootprintServiceTest {

    @DisplayName("Footprint ID를 통해 발자국의 정보를 조회할 수 있다.")
    @Test
    void findOne() {
        // given
        Footprint footprint = footprintRepository.save(FOOTPRINT());

        // when
        FindOneFootprintResponse response = footprintQueryService.findOne(member.getId(), footprint.getId());

        // then
        assertAll(
                () -> assertThat(response.memberName()).isEqualTo("name1"),
                () -> assertThat(response.pets().get(0).name()).isEqualTo("petname1"),
                () -> assertThat(response.pets().get(0).description()).isEqualTo("petdescription1"),
                () -> assertThat(response.pets().get(0).birthDate()).isEqualTo(LocalDate.now().minusYears(1)),
                () -> assertThat(response.pets().get(0).sizeType()).isEqualTo(SizeType.MEDIUM),
                () -> assertThat(response.pets().get(0).gender()).isEqualTo(Gender.MALE_NEUTERED),
                () -> assertThat(response.isMine()).isTrue()
        );
    }

    @DisplayName("현재 위치 기준 400km 이내 발자국의 수가 1개일 때, 1개의 발자국만 조회된다.")
    @Test
    void findNear() {
        // given
        Member otherMember = memberRepository.save(
                Member.builder()
                        .name("name2")
                        .email("test@test.com")
                        .build()
        );

        deviceTokenRepository.save(
                new DeviceToken(otherMember,"token")
        );

        petRepository.save(
                Pet.builder()
                        .member(otherMember)
                        .name("땡이")
                        .description("귀여운 땡이")
                        .birthDate(LocalDate.now().minusYears(1))
                        .sizeType(SizeType.MEDIUM)
                        .gender(Gender.MALE_NEUTERED)
                        .imageUrl("https://picsum.photos/200")
                        .build()
        );

        double nearLongitude = LONGITUDE_WITH_METER_FROM_ZERO(399_500);
        double farLongitude = LONGITUDE_WITH_METER_FROM_ZERO(400_500);

        footprintCommandService.save(
                member.getId(),
                new SaveFootprintRequest(0.0, nearLongitude)
        );

        footprintCommandService.save(
                otherMember.getId(),
                new SaveFootprintRequest(0.0, farLongitude)
        );

        // when
        List<FindNearFootprintResponse> nearFootprints = footprintQueryService.findNear(
                member.getId(), new FindNearFootprintRequest(0.0, 0.0));

        // then
        assertAll(
                () -> assertThat(nearFootprints).extracting(FindNearFootprintResponse::latitude)
                        .containsExactly(0.0),
                () -> assertThat(nearFootprints).extracting(FindNearFootprintResponse::longitude)
                        .containsExactly(nearLongitude)
        );
    }

    @DisplayName("현재 시간 기준 24시간 보다 이전에 생성된 발자국은 조회되지 않는다.")
    @Test
    void findNear24Hours() {
        // given
        footprintRepository.saveAll(
                List.of(
                        FOOTPRINT_STATUS_AFTER(LocalDateTime.now().minusHours(25)),
                        FOOTPRINT_STATUS_AFTER(LocalDateTime.now().minusHours(23)),
                        FOOTPRINT_STATUS_AFTER(LocalDateTime.now().minusHours(22))
                )
        );

        // when
        List<FindNearFootprintResponse> nearFootprints = footprintQueryService.findNear(
                member.getId(), new FindNearFootprintRequest(0.0, 0.0));

        // then
        assertThat(nearFootprints.size()).isEqualTo(2);
    }

    @DisplayName("주변 발자국 조회시 삭제된 발자국은 조회되지 않는다.")
    @Test
    void findNearExceptDeletedFootprint() {
        // given
        footprintRepository.saveAll(
                List.of(
                        FOOTPRINT(),
                        FOOTPRINT_DELETED(),
                        FOOTPRINT_DELETED()
                )
        );

        // when
        List<FindNearFootprintResponse> nearFootprints = footprintQueryService.findNear(
                member.getId(), new FindNearFootprintRequest(0.0, 0.0));

        // then
        assertThat(nearFootprints).hasSize(1);
    }

    @DisplayName("마지막으로 발자국을 찍은 시간을 조회할 수 있다. (발자국 O, 강아지 O)")
    @Test
    void findMyLatestFootprintTime_MyFootprintExists_PetExists() {
        // given
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        footprintRepository.saveAll(
                List.of(
                        FOOTPRINT_STATUS_AFTER(LocalDateTime.now().minusHours(25)),
                        FOOTPRINT_STATUS_AFTER(LocalDateTime.now().minusHours(23)),
                        FOOTPRINT_STATUS_AFTER(oneMinuteAgo)
                )
        );

        // when
        LocalDateTime time = footprintQueryService.findMyLatestFootprintTimeAndPetExistence(member.getId()).createdAt();

        // then
        assertAll(
                () -> assertThat(time.getHour()).isEqualTo(oneMinuteAgo.getHour()),
                () -> assertThat(time.getMinute()).isEqualTo(oneMinuteAgo.getMinute()),
                () -> assertThat(time.getSecond()).isEqualTo(oneMinuteAgo.getSecond())
        );
    }

    @DisplayName("마지막으로 발자국을 찍은 시간을 조회할 수 있다. (발자국 X, 강아지 O)")
    @Test
    void findMyLatestFootprintTime_MyFootprintDoesNotExist_PetExists() {
        // when
        FindMyLatestFootprintTimeAndPetExistenceResponse response
                = footprintQueryService.findMyLatestFootprintTimeAndPetExistence(member.getId());

        // then
        assertAll(
                () -> assertThat(response.createdAt()).isNull(),
                () -> assertThat(response.hasPet()).isTrue()
        );
    }

    @DisplayName("마지막으로 발자국을 찍은 시간을 조회할 수 있다. (발자국 X, 강아지 X)")
    @Test
    void findMyLatestFootprintTime_MyFootprintDoesNotExist_PetDoesNotExist() {
        // given
        Member memberWithoutPet = memberRepository.save(
                Member.builder()
                        .name("강아지없어요")
                        .email("no_dog@test.com")
                        .build()
        );

        // when
        FindMyLatestFootprintTimeAndPetExistenceResponse response
                = footprintQueryService.findMyLatestFootprintTimeAndPetExistence(memberWithoutPet.getId());

        // then
        assertAll(
                () -> assertThat(response.createdAt()).isNull(),
                () -> assertThat(response.hasPet()).isFalse()
        );
    }
}
