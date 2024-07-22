package com.woowacourse.friendogly.club.service;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.domain.ClubMember;
import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.dto.response.FindSearchingClubResponse;
import com.woowacourse.friendogly.club.repository.ClubMemberPetRepository;
import com.woowacourse.friendogly.club.repository.ClubMemberRepository;
import com.woowacourse.friendogly.club.repository.ClubRepository;
import com.woowacourse.friendogly.club.repository.ClubSpecification;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClubQueryService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubMemberPetRepository clubMemberPetRepository;

    public ClubQueryService(
            ClubRepository clubRepository,
            ClubMemberRepository clubMemberRepository,
            ClubMemberPetRepository clubMemberPetRepository
    ) {
        this.clubRepository = clubRepository;
        this.clubMemberRepository = clubMemberRepository;
        this.clubMemberPetRepository = clubMemberPetRepository;
    }

    public List<FindSearchingClubResponse> findSearching(FindSearchingClubRequest request) {
        Specification<Club> spec = ClubSpecification.where()
                .equalsAddress(request.address())
                .hasGenders(request.genderParams())
                .hasSizeTypes(request.sizeParams())
                .build();

        return clubRepository.findAll(spec).stream()
                .map(club -> {
                    List<String> overviewPetImages = clubMemberRepository.findAllByClubId(club.getId()).stream()
                            .map(ClubMember::findOverviewPetImage)
                            .toList();
                    return new FindSearchingClubResponse(
                            club,
                            clubMemberRepository.countByClubId(club.getId()),
                            overviewPetImages
                    );
                })
                .toList();
    }
}


