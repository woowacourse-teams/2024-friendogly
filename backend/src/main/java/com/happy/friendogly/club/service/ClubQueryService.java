package com.happy.friendogly.club.service;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.domain.FilterCondition;
import com.happy.friendogly.club.dto.request.FindClubByFilterRequest;
import com.happy.friendogly.club.dto.response.FindClubByFilterResponse;
import com.happy.friendogly.club.dto.response.FindClubMineResponse;
import com.happy.friendogly.club.dto.response.FindClubResponse;
import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.club.repository.ClubSpecification;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.pet.repository.PetRepository;
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

    public List<FindClubByFilterResponse> findByFilter(Long memberId, FindClubByFilterRequest request) {
        Member member = memberRepository.getById(memberId);
        List<Pet> pets = petRepository.findByMemberId(memberId);

        Specification<Club> spec = ClubSpecification.where()
                .equalsProvince(request.province())
                .hasGenders(Gender.toGenders(request.genderParams()))
                .hasSizeTypes(SizeType.toSizeTypes(request.sizeParams()))
                .build();

        List<Club> clubs = clubRepository.findAll(spec);

        FilterCondition filterCondition = FilterCondition.from(request.filterCondition());

        return filterClubs(clubs.stream(), filterCondition, member, pets)
                .map(club -> new FindClubByFilterResponse(club, collectOverviewPetImages(club)))
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

    public List<FindClubMineResponse> findMine(Long memberId) {
        List<Club> participantClubs = clubRepository.findAllByParticipantMemberId(memberId);
        Member member = memberRepository.getById(memberId);

        return participantClubs.stream()
                .filter(club -> club.isOwner(member))
                .map(club -> new FindClubMineResponse(club, collectOverviewPetImages(club)))
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
