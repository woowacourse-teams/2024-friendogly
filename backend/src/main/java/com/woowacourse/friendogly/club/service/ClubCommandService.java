package com.woowacourse.friendogly.club.service;


import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.domain.ClubMember;
import com.woowacourse.friendogly.club.domain.ClubPet;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.club.repository.ClubMemberRepository;
import com.woowacourse.friendogly.club.repository.ClubPetRepository;
import com.woowacourse.friendogly.club.repository.ClubRepository;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClubCommandService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubPetRepository clubPetRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    public ClubCommandService(
            ClubRepository clubRepository,
            ClubMemberRepository clubMemberRepository,
            ClubPetRepository clubPetRepository,
            MemberRepository memberRepository,
            PetRepository petRepository
    ) {
        this.clubRepository = clubRepository;
        this.clubMemberRepository = clubMemberRepository;
        this.clubPetRepository = clubPetRepository;
        this.memberRepository = memberRepository;
        this.petRepository = petRepository;
    }

    public SaveClubResponse save(Long memberId, SaveClubRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FriendoglyException("회원 정보를 찾지 못했습니다."));

        Club newClub = clubRepository.save(request.toEntity(member));
        clubMemberRepository.save(
                ClubMember.builder()
                        .club(newClub)
                        .member(member)
                        .build()
        );

        List<Pet> participatingPets = petRepository.findByMemberId(memberId).stream()
                .filter(pet -> request.participatingPetsId().contains(pet.getId()))
                .toList();

        participatingPets.stream()
                .map(pet -> ClubPet.builder()
                        .club(newClub)
                        .pet(pet)
                        .build())
                .forEach(clubPetRepository::save);

        List<String> petImageUrls = participatingPets.stream()
                .map(pet -> pet.getImageUrl().getValue())
                .toList();

        return new SaveClubResponse(newClub, 1, petImageUrls);
    }
}
