package com.woowacourse.friendogly.club.service;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.domain.ClubMember;
import com.woowacourse.friendogly.club.domain.ClubPet;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.support.ServiceTest;
import java.time.LocalDate;
import java.util.Set;


public abstract class ClubServiceTest extends ServiceTest {

    protected final String address = "서울특별시 송파구 신청동";
    protected final Set<Gender> allowedGenders = Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED);
    protected final Set<SizeType> allowedSizes = Set.of(SizeType.SMALL);
    protected final String petImageUrl = "https://PetimageUrl.com";

    protected final Member member = Member.builder()
            .name("브라운")
            .email("woowha@gmail.com")
            .build();

    protected final Pet pet = Pet.builder()
            .member(member)
            .name("땡이")
            .imageUrl(petImageUrl)
            .birthDate(LocalDate.of(2020, 11, 1))
            .description("귀여운 땡이 >.<")
            .gender(Gender.FEMALE)
            .sizeType(SizeType.SMALL)
            .build();


    protected Club createSavedClub(Set<Gender> genders, Set<SizeType> sizes) {
        memberRepository.save(member);
        petRepository.save(pet);

        Club club = Club.create(
                "강아지 산책시키실 분 모아요.",
                "매주 주말에 정기적으로 산책 모임하실분만",
                address,
                5,
                member,
                genders,
                sizes,
                "https://image.com");

        Club savedClub = clubRepository.save(club);
        ClubMember clubMember = ClubMember.builder()
                .member(member)
                .club(club)
                .build();

        ClubPet clubPet = ClubPet.builder()
                .club(club)
                .pet(pet)
                .build();
        clubMemberRepository.save(clubMember);
        clubPetRepository.save(clubPet);

        return savedClub;
    }
}
