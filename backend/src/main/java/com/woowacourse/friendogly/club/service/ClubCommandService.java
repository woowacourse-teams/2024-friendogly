package com.woowacourse.friendogly.club.service;


import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.dto.request.SaveClubMemberRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.SaveClubMemberResponse;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
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
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    public ClubCommandService(
            ClubRepository clubRepository,
            MemberRepository memberRepository,
            PetRepository petRepository
    ) {
        this.clubRepository = clubRepository;
        this.memberRepository = memberRepository;
        this.petRepository = petRepository;
    }

    public SaveClubResponse save(Long memberId, SaveClubRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FriendoglyException("회원 정보를 찾지 못했습니다."));

        Club newClub = clubRepository.save(request.toEntity(member));

        List<Pet> participatingPets = request.participatingPetsId().stream()
                .map(id -> petRepository.findById(id).orElseThrow(() -> new FriendoglyException("강아지 정보를 찾지 못했습니다.")))
                .toList();

        List<String> petImageUrls = participatingPets.stream()
                .map(pet -> pet.getImageUrl().getValue())
                .toList();

        newClub.addClubPet(participatingPets);

        return new SaveClubResponse(newClub, 1, petImageUrls);
    }

    public SaveClubMemberResponse saveClubMember(Long clubId, Long memberId, SaveClubMemberRequest request) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new FriendoglyException("모임 정보를 찾지 못했습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FriendoglyException("회원 정보를 찾지 못했습니다."));

        club.addClubMember(member);
        List<Pet> pets = mapToPets(request.participatingPetsId());
        club.addClubPet(pets);

        return new SaveClubMemberResponse(1L);
    }

    private List<Pet> mapToPets(List<Long> participatingPetsId) {
        return participatingPetsId.stream()
                .map(id -> petRepository.findById(id).orElseThrow(() -> new FriendoglyException("강아지 정보를 찾지 못했습니다.")))
                .toList();
    }

    public void deleteClubMember(Long clubId, Long memberId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new FriendoglyException("모임 정보를 찾지 못했습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FriendoglyException("회원 정보를 찾지 못했습니다."));
        club.removeClubMember(member);
    }
}
