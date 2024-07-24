package com.woowacourse.friendogly.club.service;

import static java.util.stream.Collectors.toList;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.domain.ClubPet;
import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.dto.response.FindSearchingClubResponse;
import com.woowacourse.friendogly.club.repository.ClubMemberRepository;
import com.woowacourse.friendogly.club.repository.ClubPetRepository;
import com.woowacourse.friendogly.club.repository.ClubRepository;
import com.woowacourse.friendogly.club.repository.ClubSpecification;
import com.woowacourse.friendogly.pet.domain.Pet;
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
    private final ClubMemberRepository clubMemberRepository;
    private final ClubPetRepository clubPetRepository;

    public ClubQueryService(
            ClubRepository clubRepository,
            ClubMemberRepository clubMemberRepository,
            ClubPetRepository clubPetRepository
    ) {
        this.clubRepository = clubRepository;
        this.clubMemberRepository = clubMemberRepository;
        this.clubPetRepository = clubPetRepository;
    }

    public List<FindSearchingClubResponse> findSearching(FindSearchingClubRequest request) {
        Specification<Club> spec = ClubSpecification.where()
                .equalsAddress(request.address())
                .hasGenders(request.genderParams())
                .hasSizeTypes(request.sizeParams())
                .build();

        return clubRepository.findAll(spec).stream()
                .map(club -> new FindSearchingClubResponse(
                        club,
                        clubMemberRepository.countByClubId(club.getId()),
                        collectOverviewPetImages(club)
                ))
                .toList();
    }

    private List<String> collectOverviewPetImages(Club club) {
        Map<Long, List<Pet>> groupPetsByMemberId = clubPetRepository.findAllByClubId(club.getId()).stream()
                .map(ClubPet::getPet)
                .collect(Collectors.groupingBy(pet -> pet.getMember().getId()));

        return groupPetsByMemberId.values().stream()
                .map(petList -> petList.get(0).getImageUrl())
                .collect(toList());
    }
}


