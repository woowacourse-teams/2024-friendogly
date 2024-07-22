package com.woowacourse.friendogly.footprint.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.support.ServiceTest;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class FootprintCommandServiceTest extends ServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FootprintCommandService footprintCommandService;

    @DisplayName("발자국 저장")
    @Test
    void save() {
        // given
        Member member = memberRepository.save(
                Member.builder()
                        .name("name")
                        .email("test@test.com")
                        .build()
        );

        petRepository.save(
                Pet.builder()
                        .member(member)
                        .name("땡이")
                        .description("귀여운 땡이")
                        .birthDate(LocalDate.now().minusYears(1))
                        .sizeType(SizeType.MEDIUM)
                        .gender(Gender.MALE_NEUTERED)
                        .imageUrl("https://picsum.photos/200")
                        .build()
        );

        // when
        footprintCommandService.save(member.getId(), new SaveFootprintRequest(30.0, 30.0));

        // then
        assertThat(footprintRepository.findAll()).hasSize(1);
    }

    @DisplayName("강아지를 등록하지 않은 사용자는 발자국을 남길 수 없다.")
    @Test
    void save_Fail_NoPets() {
        // given
        Member member = memberRepository.save(
                Member.builder()
                        .name("name")
                        .email("test@test.com")
                        .build()
        );

        // when - then
        assertThatThrownBy(
                () -> footprintCommandService.save(
                        member.getId(),
                        new SaveFootprintRequest(90.000, 90.000)
                )
        ).isInstanceOf(FriendoglyException.class)
                .hasMessage("펫을 등록해야만 발자국을 생성할 수 있습니다.");
    }

    @DisplayName("발자국 저장 실패 - 존재하지 않는 Member ID")
    @Test
    void save_Fail_IllegalMemberId() {
        assertThatThrownBy(
                () -> footprintCommandService.save(
                        -1L,
                        new SaveFootprintRequest(30.0, 30.0)
                )
        ).isInstanceOf(FriendoglyException.class)
                .hasMessage("존재하지 않는 사용자 ID입니다.");
    }

    @DisplayName("발자국 저장 실패 - 30초 전에 이미 발자국을 남긴 경우")
    @Test
    void save_Fail_TooOftenSave() {
        Member member = memberRepository.save(
                Member.builder()
                        .name("name")
                        .email("test@test.com")
                        .build()
        );

        petRepository.save(
                Pet.builder()
                        .member(member)
                        .name("땡이")
                        .description("귀여운 땡이")
                        .birthDate(LocalDate.now().minusYears(1))
                        .sizeType(SizeType.MEDIUM)
                        .gender(Gender.MALE_NEUTERED)
                        .imageUrl("https://picsum.photos/200")
                        .build()
        );

        jdbcTemplate.update("""
                INSERT INTO footprint (member_id, latitude, longitude, created_at, is_deleted)
                VALUES
                (?, 0.00000, 0.00000, TIMESTAMPADD(SECOND, -29, NOW()), FALSE)
                """, member.getId());

        assertThatThrownBy(
                () -> footprintCommandService.save(
                        member.getId(),
                        new SaveFootprintRequest(30.0, 30.0)
                )
        ).isInstanceOf(FriendoglyException.class)
                .hasMessage("마지막 발자국을 찍은 뒤 30초가 경과되지 않았습니다.");
    }
}
