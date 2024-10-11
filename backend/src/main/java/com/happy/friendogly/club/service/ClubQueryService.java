package com.happy.friendogly.club.service;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.domain.FilterCondition;
import com.happy.friendogly.club.dto.request.FindClubByFilterRequest;
import com.happy.friendogly.club.dto.response.FindClubByFilterResponse;
import com.happy.friendogly.club.dto.response.FindClubOwningResponse;
import com.happy.friendogly.club.dto.response.FindClubPageByFilterResponse;
import com.happy.friendogly.club.dto.response.FindClubParticipatingResponse;
import com.happy.friendogly.club.dto.response.FindClubResponse;
import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.pet.repository.PetRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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

    public FindClubPageByFilterResponse findByFilter(Long memberId, FindClubByFilterRequest request) {
        Member member = memberRepository.getById(memberId);
        List<Pet> pets = petRepository.findByMemberId(memberId);

        FilterCondition filterCondition = FilterCondition.from(request.filterCondition());
        LocalDateTime lastFoundCreatedAt = request.lastFoundCreatedAt();
        Long lastFoundId = request.lastFoundId();

        List<Club> result = new ArrayList<>();
        Slice<Club> clubSlice = fetchClubSlice(request, lastFoundCreatedAt, lastFoundId);
        List<Club> filteredClubs = filterClubs(clubSlice, filterCondition, member, pets);
        addFilteredResults(request, filteredClubs, result);

        while (result.size() < request.pageSize() && clubSlice.hasNext()) {
            lastFoundCreatedAt = updateLastFoundCreatedAt(lastFoundCreatedAt, result);
            lastFoundId = updateLastFoundId(lastFoundId, result);

            clubSlice = fetchClubSlice(request, lastFoundCreatedAt, lastFoundId);
            filteredClubs = filterClubs(clubSlice, filterCondition, member, pets);
            addFilteredResults(request, filteredClubs, result);
        }

        List<FindClubByFilterResponse> response = result.stream()
                .map(club -> new FindClubByFilterResponse(club, collectOverviewPetImages(club)))
                .toList();

        return new FindClubPageByFilterResponse(clubSlice.isLast(), response);
    }

    private Slice<Club> fetchClubSlice(FindClubByFilterRequest request, LocalDateTime lastFoundCreatedAt, Long lastFoundId) {
        // TODO: ===== 클라이언트 페이징 적용 완료되면 제거 =====
        int pageSize = request.pageSize();
        LocalDateTime createdAt = lastFoundCreatedAt;
        Long id = lastFoundId;

        if (request.pageSize() == null) {
            pageSize = Integer.MAX_VALUE;
        }
        if (lastFoundCreatedAt == null) {
            createdAt = LocalDateTime.of(9999, 12, 31, 11, 59);
        }
        if (lastFoundId == null) {
            id = Long.MAX_VALUE;
        }
        // ==================================================

        return clubRepository.findAllBy(
                request.province(),
                Gender.toGenders(request.genderParams()),
                SizeType.toSizeTypes(request.sizeParams()),
//                PageRequest.ofSize(request.pageSize()),
//                lastFoundCreatedAt,
//                lastFoundId
                PageRequest.ofSize(pageSize),
                createdAt,
                id
        );
    }

    private List<Club> filterClubs(Slice<Club> clubSlice, FilterCondition filterCondition, Member member, List<Pet> pets) {
        return clubSlice.getContent()
                .stream()
                .filter(club -> isConditionMatch(club, filterCondition, member, pets))
                .toList();
    }

    private boolean isConditionMatch(Club club, FilterCondition filterCondition, Member member, List<Pet> pets) {
        if (filterCondition.isAbleToJoin()) {
            return club.isJoinable(member, pets);
        }
        if (filterCondition.isOpen()) {
            return club.isOpen();
        }
        return true;
    }

    private void addFilteredResults(FindClubByFilterRequest request, List<Club> filteredClubs, List<Club> result) {
        for (Club club : filteredClubs) {
            if (result.size() >= request.pageSize()) {
                break;
            }
            result.add(club);
        }
    }

    private LocalDateTime updateLastFoundCreatedAt(LocalDateTime lastFoundCreatedAt, List<Club> result) {
        if (result.isEmpty()) {
            return lastFoundCreatedAt;
        }
        return result.get(result.size() - 1).getCreatedAt();
    }

    private Long updateLastFoundId(Long lastFoundId, List<Club> result) {
        if (result.isEmpty()) {
            return lastFoundId;
        }
        return result.get(result.size() - 1).getId();
    }

    public List<FindClubOwningResponse> findOwning(Long memberId) {
        List<Club> participatingClubs = clubRepository.findAllByParticipatingMemberId(memberId);
        Member member = memberRepository.getById(memberId);
        return participatingClubs.stream()
                .filter(club -> club.isOwner(member))
                .map(club -> new FindClubOwningResponse(club, collectOverviewPetImages(club)))
                .toList();
    }

    public List<FindClubParticipatingResponse> findParticipating(Long memberId) {
        return clubRepository.findAllByParticipatingMemberId(memberId)
                .stream()
                .map(club -> new FindClubParticipatingResponse(club, collectOverviewPetImages(club)))
                .toList();
    }

    private List<String> collectOverviewPetImages(Club club) {
        Map<Long, List<Pet>> groupPetsByMemberId = club.getClubPets()
                .stream()
                .map(clubPet -> clubPet.getClubPetId().getPet())
                .collect(Collectors.groupingBy(pet -> pet.getMember().getId()));

        return groupPetsByMemberId.values().stream()
                .map(petList -> petList.get(0).getImageUrl())
                .toList();
    }

    public FindClubResponse findById(Long memberId, Long clubId) {
        Club club = clubRepository.getById(clubId);
        Member member = memberRepository.getById(memberId);
        List<Pet> pets = petRepository.findByMemberId(memberId);

        return new FindClubResponse(club, member, pets);
    }
}
