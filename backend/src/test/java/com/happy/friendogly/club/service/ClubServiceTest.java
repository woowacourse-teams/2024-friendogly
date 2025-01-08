package com.happy.friendogly.club.service;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.support.ServiceTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


public abstract class ClubServiceTest extends ServiceTest {

    protected final String province = "서울특별시";
    protected final String city = "송파구";
    protected final String village = "신천동";
    protected final String petImageUrl = "https://PetimageUrl.com";

    protected Member createSavedMember() {
        return memberRepository.save(Member.builder()
                .name("브라운")
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


    protected Club createSavedClub(Member member, List<Pet> pets, Set<Gender> genders, Set<SizeType> sizes) {
        Club club = Club.create(
                "강아지 산책시키실 분 모아요.",
                "매주 주말에 정기적으로 산책 모임하실분만",
                province,
                city,
                village,
                5,
                member,
                genders,
                sizes,
                "https://image.com",
                pets);

        return clubRepository.save(club);
    }
}
