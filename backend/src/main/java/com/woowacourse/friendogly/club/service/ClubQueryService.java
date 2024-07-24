package com.woowacourse.friendogly.club.service;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.domain.ClubPet;
import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.dto.response.FindSearchingClubResponse;
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

    public ClubQueryService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
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
        Map<Long, List<Pet>> groupPetsByMemberId = club.getClubPets().stream()
                .map(ClubPet::getPet)
                .collect(Collectors.groupingBy(pet -> pet.getMember().getId()));

        return groupPetsByMemberId.values().stream()
                .map(petList -> petList.get(0).getImageUrl().getValue())
                .toList();
    }
}
