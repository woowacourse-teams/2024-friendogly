package com.woowacourse.friendogly.club.service;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.dto.response.FindClubResponse;
import com.woowacourse.friendogly.club.dto.response.FindSearchingClubResponse;
import com.woowacourse.friendogly.club.repository.ClubRepository;
import com.woowacourse.friendogly.club.repository.ClubSpecification;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClubQueryService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    public ClubQueryService(
            ClubRepository clubRepository,
            MemberRepository memberRepository,
            PetRepository petRepository
    ) {
        this.clubRepository = clubRepository;
        this.memberRepository = memberRepository;
        this.petRepository = petRepository;
    }

    public List<FindSearchingClubResponse> findSearching(FindSearchingClubRequest request) {
        Specification<Club> spec = ClubSpecification.where()
                .equalsAddress(request.address())
                .hasGenders(request.genderParams())
                .hasSizeTypes(request.sizeParams())
                .build();

        return clubRepository.findAll(spec).stream()
                .map(club -> new FindSearchingClubResponse(club, collectOverviewPetImages(club)))
                .toList();
    }

    private List<String> collectOverviewPetImages(Club club) {
        Map<Long, List<Pet>> groupPetsByMemberId = club.getClubPets()
                .stream()
                .map(clubPet -> clubPet.getClubPetPk().getPet())
                .collect(Collectors.groupingBy(pet -> pet.getMember().getId()));

        return groupPetsByMemberId.values().stream()
                .map(petList -> petList.get(0).getImageUrl())
                .toList();
    }

    public FindClubResponse findById(Long memberId, Long id) {
        Club club = clubRepository.getById(id);
        Member member = memberRepository.getById(memberId);
        List<Pet> pets = petRepository.findByMemberId(memberId);

        return new FindClubResponse(club, member, pets);
    }
}
