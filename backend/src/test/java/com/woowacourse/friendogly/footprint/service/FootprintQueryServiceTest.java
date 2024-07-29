package com.woowacourse.friendogly.footprint.service;

import static com.woowacourse.friendogly.footprint.domain.WalkStatus.GOING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.footprint.domain.Location;
import com.woowacourse.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.response.FindMyLatestFootprintTimeAndPetExistenceResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindOneFootprintResponse;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// TODO: Member, Dog 테스트 픽스처 생성
class FootprintQueryServiceTest extends FootprintServiceTest {

    @DisplayName("Footprint ID를 통해 발자국의 정보를 조회할 수 있다. (발자국 사진을 찍은 경우 - 발자국 사진 URL 조회)")
    @Transactional
    @Test
    void findOne() {
        // given
        Footprint footprint = footprintRepository.save(
                Footprint.builder()
                        .member(member)
                        .walkStatus(GOING)
                        .location(new Location(0.0, 0.0))
                        .build()
        );

        // when
        FindOneFootprintResponse response = footprintQueryService.findOne(member.getId(), footprint.getId());

        // then
        assertAll(
                () -> assertThat(response.memberName()).isEqualTo("name1"),
                () -> assertThat(response.petName()).isEqualTo("petname1"),
                () -> assertThat(response.petDescription()).isEqualTo("petdescription1"),
                () -> assertThat(response.petBirthDate()).isEqualTo(LocalDate.now().minusYears(1)),
                () -> assertThat(response.petSizeType()).isEqualTo(SizeType.MEDIUM),
                () -> assertThat(response.petGender()).isEqualTo(Gender.MALE_NEUTERED),
                () -> assertThat(response.isMine()).isTrue()
        );
    }

    @DisplayName("Footprint ID를 통해 발자국의 정보를 조회할 수 있다. (발자국 사진을 안 찍은 경우 - 펫 사진 URL 조회)")
    @Test
    void findOne_NoTakePicture() {
        // given
        Footprint footprint = footprintRepository.save(
                Footprint.builder()
                        .walkStatus(GOING)
                        .member(member)
                        .location(new Location(0.0, 0.0))
                        .build()
        );

        // when
        FindOneFootprintResponse response = footprintQueryService.findOne(member.getId(), footprint.getId());

        // then
        assertAll(
                () -> assertThat(response.memberName()).isEqualTo("name1"),
                () -> assertThat(response.petName()).isEqualTo("petname1"),
                () -> assertThat(response.petDescription()).isEqualTo("petdescription1"),
                () -> assertThat(response.petBirthDate()).isEqualTo(LocalDate.now().minusYears(1)),
                () -> assertThat(response.petSizeType()).isEqualTo(SizeType.MEDIUM),
                () -> assertThat(response.petGender()).isEqualTo(Gender.MALE_NEUTERED),
                () -> assertThat(response.isMine()).isTrue()
        );
    }

    @DisplayName("현재 위치 기준 1km 이내 발자국의 수가 1개일 때, 1개의 발자국만 조회된다.")
    @Test
    void findNear() {
        // given
        Member otherMember = memberRepository.save(
                Member.builder()
                        .name("name2")
                        .email("test@test.com")
                        .build()
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

        double nearLongitude = 0.008993216;
        double farLongitude = 0.009001209;

        // 999m 떨어진 발자국 저장
        footprintCommandService.save(
                member.getId(),
                new SaveFootprintRequest(0.0, nearLongitude)
        );

        // 1001m 떨어진 발자국 저장
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
        jdbcTemplate.update("""
                INSERT INTO footprint (member_id, latitude, longitude, walk_status, created_at, is_deleted)
                VALUES
                (?, 0.00000, 0.00000, 'END', TIMESTAMPADD(HOUR, -25, NOW()), FALSE),
                (?, 0.00000, 0.00000, 'END', TIMESTAMPADD(HOUR, -23, NOW()), FALSE),
                (?, 0.00000, 0.00000, 'END', TIMESTAMPADD(HOUR, -22, NOW()), FALSE);
                """, member.getId(), member.getId(), member.getId());

        // when
        List<FindNearFootprintResponse> nearFootprints = footprintQueryService.findNear(
                member.getId(), new FindNearFootprintRequest(0.0, 0.0));

        // then
        assertAll(
                () -> assertThat(nearFootprints).extracting(FindNearFootprintResponse::latitude)
                        .containsExactly(0.00000, 0.00000),
                () -> assertThat(nearFootprints).extracting(FindNearFootprintResponse::longitude)
                        .containsExactly(0.00000, 0.00000)
        );
    }

    @DisplayName("마지막으로 발자국을 찍은 시간을 조회할 수 있다. (발자국 O, 강아지 O)")
    @Test
    void findMyLatestFootprintTime_MyFootprintExists_PetExists() {
        // given
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);

        jdbcTemplate.update("""
                INSERT INTO footprint (member_id, latitude, longitude, walk_status, created_at, is_deleted)
                VALUES
                (?, 0.00000, 0.00000, 'END', TIMESTAMPADD(HOUR, -25, NOW()), FALSE),
                (?, 0.11111, 0.11111, 'END', TIMESTAMPADD(HOUR, -23, NOW()), FALSE),
                (?, 0.22222, 0.22222, 'END', ?, FALSE);
                """, member.getId(), member.getId(), member.getId(), oneMinuteAgo);

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
