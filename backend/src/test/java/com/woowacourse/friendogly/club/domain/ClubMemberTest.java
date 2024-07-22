package com.woowacourse.friendogly.club.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClubMemberTest {

    private final Member member = Member.builder()
            .name("브라운")
            .email("woowha@gmail.com")
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
            "서울특별시 송파구 신청동",
            5,
            member,
            Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
            Set.of(SizeType.SMALL),
            "http:/image.com"
    );

    @DisplayName("모임-회원 매핑 테이블 엔티티 생성 테스트")
    @Test
    void create() {

        assertThatCode(() -> ClubMember.builder()
                .club(club)
                .member(member)
                .build())
                .doesNotThrowAnyException();
    }

    @DisplayName("모임 정보가 없으면 예외가 발생한다.")
    @Test
    void create_FailNullClub() {
        assertThatThrownBy(() -> ClubMember.builder()
                .club(null)
                .member(member)
                .build())
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("모임 정보는 필수입니다.");
    }

    @DisplayName("참여 회원 정보가 없으면 예외가 발생한다.")
    @Test
    void create_FailNullMember() {
        assertThatThrownBy(() -> ClubMember.builder()
                .club(club)
                .member(null)
                .build())
                .isInstanceOf(FriendoglyException.class)
                .hasMessage("모임에 참여하는 회원 정보는 필수입니다.");
    }

    @DisplayName("리스트업에 나오는 참여 강아지 사진을 반환한다.")
    @Test
    void findClubOverviewPetImage() {
        ClubMember clubMember = ClubMember.builder()
                .club(club)
                .member(member)
                .build();

        clubMember.addClubMemberPets(ClubMemberPet.builder()
                .clubMember(clubMember)
                .pet(pet)
                .build());

        assertThat(clubMember.findOverviewPetImage()).isEqualTo(pet.getImageUrl().getValue());
    }

    @DisplayName("참여 중인 회원이 어떤 강아지도 데리고 가지 않는다면 null을 반환한다.")
    @Test
    void findClubOverviewPetImage_FailNonPets() {
        ClubMember clubMember = ClubMember.builder()
                .club(club)
                .member(member)
                .build();

        assertThat(clubMember.findOverviewPetImage()).isNull();
    }
}
