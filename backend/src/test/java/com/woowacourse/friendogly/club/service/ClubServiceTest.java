package com.woowacourse.friendogly.club.service;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.domain.ClubMember;
import com.woowacourse.friendogly.club.domain.ClubMemberPet;
import com.woowacourse.friendogly.club.repository.ClubMemberPetRepository;
import com.woowacourse.friendogly.club.repository.ClubMemberRepository;
import com.woowacourse.friendogly.club.repository.ClubRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public abstract class ClubServiceTest {

    @Autowired
    protected ClubRepository clubRepository;

    @Autowired
    protected ClubMemberRepository clubMemberRepository;

    @Autowired
    protected ClubMemberPetRepository clubMemberPetRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected PetRepository petRepository;

    protected final String address = "서울특별시 송파구 신청동";
    protected final Set<Gender> allowedGenders = Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED);
    protected final Set<SizeType> allowedSizes = Set.of(SizeType.SMALL);
    protected final String petImageUrl = "https://PetimageUrl.com";
    private final Member member = Member.builder()
            .name("브라운")
            .email("woowha@gmail.com")
            .build();

    private final Pet pet = Pet.builder()
            .member(member)
            .name("땡이")
            .imageUrl(petImageUrl)
            .birthDate(LocalDate.of(2020, 11, 1))
            .description("귀여운 땡이 >.<")
            .gender(Gender.FEMALE)
            .sizeType(SizeType.SMALL)
            .build();

    private final Club club = Club.create(
            "강아지 산책시키실 분 모아요.",
            "매주 주말에 정기적으로 산책 모임하실분만",
            address,
            5,
            member,
            allowedGenders,
            allowedSizes,
            "https://image.com");

    @BeforeEach
    void clearDB() {
        memberRepository.deleteAll();
        petRepository.deleteAll();
        clubRepository.deleteAll();
        clubMemberRepository.deleteAll();
        clubMemberPetRepository.deleteAll();
    }

    protected Club saveNewClub() {
        memberRepository.save(member);
        petRepository.save(pet);
        Club savedClub = clubRepository.save(club);
        ClubMember clubMember = ClubMember.builder()
                .member(member)
                .club(club)
                .build();

        ClubMemberPet clubMemberPet = ClubMemberPet.builder()
                .clubMember(clubMember)
                .pet(pet)
                .build();
        clubMemberRepository.save(clubMember);
        clubMemberPetRepository.save(clubMemberPet);

        return savedClub;
    }
}
