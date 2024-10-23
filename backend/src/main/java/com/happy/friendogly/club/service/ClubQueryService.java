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
import com.happy.friendogly.exception.FriendoglyException;
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

    private static final int MAX_PAGE_SIZE = 20;

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
            lastFoundCreatedAt = updateLastFoundCreatedAt(clubSlice, result);
            lastFoundId = updateLastFoundId(clubSlice, result);

            clubSlice = fetchClubSlice(request, lastFoundCreatedAt, lastFoundId);
            filteredClubs = filterClubs(clubSlice, filterCondition, member, pets);
            addFilteredResults(request, filteredClubs, result);
        }

        List<FindClubByFilterResponse> response = result.stream()
                .map(club -> new FindClubByFilterResponse(club, collectOverviewPetImages(club)))
                .toList();

        boolean isLastPage = result.size() < request.pageSize();
        return new FindClubPageByFilterResponse(isLastPage, response);
    }

    private Slice<Club> fetchClubSlice(FindClubByFilterRequest request, LocalDateTime lastFoundCreatedAt, Long lastFoundId) {
        Integer pageSize = request.pageSize();
        if (pageSize > MAX_PAGE_SIZE) {
            throw new FriendoglyException(String.format("페이지 크기는 %d를 넘을 수 없습니다.", MAX_PAGE_SIZE));
        }

        return clubRepository.findAllBy(
                request.province(),
                Gender.toGenders(request.genderParams()),
                SizeType.toSizeTypes(request.sizeParams()),
                PageRequest.ofSize(pageSize * 2),
                lastFoundCreatedAt,
                lastFoundId
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

    private LocalDateTime updateLastFoundCreatedAt(Slice<Club> clubSlice, List<Club> result) {
        if (result.isEmpty()) {
            return clubSlice.getContent().get(clubSlice.getSize() - 1).getCreatedAt();
        }
        return result.get(result.size() - 1).getCreatedAt();
    }

    private Long updateLastFoundId(Slice<Club> clubSlice, List<Club> result) {
        if (result.isEmpty()) {
            return clubSlice.getContent().get(clubSlice.getSize() - 1).getId();
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
