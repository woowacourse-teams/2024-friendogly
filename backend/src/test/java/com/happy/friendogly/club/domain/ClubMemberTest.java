package com.happy.friendogly.club.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClubMemberTest {

    private final Member member = Member.builder()
            .name("브라운")
            .build();

    private final Pet pet = Pet.builder()
            .member(member)
            .name("땡이")
            .imageUrl("https://imageUrl.com")
            .birthDate(LocalDate.of(2020, 11, 1))
            .description("귀여운 땡이 >.<")
            .gender(Gender.FEMALE)
            .sizeType(SizeType.SMALL)
            .build();

    private final Club club = Club.create(
            "강아지 산책시키실 분 모아요.",
            "매주 주말에 정기적으로 산책 모임하실분만",
            "서울특별시",
            "송파구",
            "신청동",
            5,
            member,
            Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
            Set.of(SizeType.SMALL),
            "http:/image.com",
            List.of(pet)
    );

    @DisplayName("모임-회원 매핑 테이블 엔티티 생성 테스트")
    @Test
    void create() {

        assertThatCode(() -> ClubMember.create(club, member))
                .doesNotThrowAnyException();
    }

    @DisplayName("모임 정보가 없으면 예외가 발생한다.")
    @Test
    void create_FailNullClub() {
        assertThatThrownBy(() -> ClubMember.create(null, member))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("모임 정보는 필수입니다.");
    }

    @DisplayName("참여 회원 정보가 없으면 예외가 발생한다.")
    @Test
    void create_FailNullMember() {
        assertThatThrownBy(() -> ClubMember.create(club, null))
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("모임에 참여하는 회원 정보는 필수입니다.");
    }
}
