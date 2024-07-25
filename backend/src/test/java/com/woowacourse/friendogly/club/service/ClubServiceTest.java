package com.woowacourse.friendogly.club.service;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.support.ServiceTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


public abstract class ClubServiceTest extends ServiceTest {

    protected final String address = "서울특별시 송파구 신청동";
    protected final String petImageUrl = "https://PetimageUrl.com";

    protected Member createSavedMember() {
        return memberRepository.save(Member.builder()
                .name("브라운")
                .email("woowha@gmail.com")
                .build());
    }

    protected Pet createSavedPet(Member owner) {
        return petRepository.save(Pet.builder()
                .member(owner)
                .name("땡이")
                .imageUrl(petImageUrl)
                .birthDate(LocalDate.of(2020, 11, 1))
                .description("귀여운 땡이 >.<")
                .gender(Gender.FEMALE)
                .sizeType(SizeType.SMALL)
                .build());
    }


    protected Club createSavedClub(Member member, Pet pet, Set<Gender> genders, Set<SizeType> sizes) {
        Club club = Club.create(
                "강아지 산책시키실 분 모아요.",
                "매주 주말에 정기적으로 산책 모임하실분만",
                address,
                5,
                member,
                genders,
                sizes,
                "https://image.com",
                List.of(pet));

        return clubRepository.save(club);
    }
}
