package com.woowacourse.friendogly.club.service;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.domain.FilterCondition;
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
import java.util.stream.Stream;
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

    public List<FindSearchingClubResponse> findFindByFilter(Long memberId, FindSearchingClubRequest request) {
        Member member = memberRepository.getById(memberId);
        List<Pet> pets = petRepository.findByMemberId(memberId);

        Specification<Club> spec = ClubSpecification.where()
                .equalsProvince(request.province())
                .equalsCity(request.city())
                .equalsVillage(request.village())
                .hasGenders(request.genderParams())
                .hasSizeTypes(request.sizeParams())
                .build();

        List<Club> clubs = clubRepository.findAll(spec);

        FilterCondition filterCondition = FilterCondition.from(request.filterCondition());

        return filterClubs(clubs.stream(), filterCondition, member, pets)
                .map(club -> new FindSearchingClubResponse(club, collectOverviewPetImages(club)))
                .toList();
    }

    private Stream<Club> filterClubs(
            Stream<Club> clubStream,
            FilterCondition filterCondition,
            Member member,
            List<Pet> pets
    ) {
        if (filterCondition.isAbleToJoin()) {
            return clubStream.filter(club -> club.isJoinable(member, pets));
        }
        if (filterCondition.isOpen()) {
            return clubStream.filter(Club::isOpen);
        }
        return clubStream;
    }

    public FindClubResponse findById(Long memberId, Long id) {
        Club club = clubRepository.getById(id);
        Member member = memberRepository.getById(memberId);
        List<Pet> pets = petRepository.findByMemberId(memberId);

        return new FindClubResponse(club, member, pets);
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
}
