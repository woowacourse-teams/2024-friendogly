package com.happy.friendogly.club.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.domain.FilterCondition;
import com.happy.friendogly.club.dto.request.FindClubByFilterRequest;
import com.happy.friendogly.club.dto.request.SaveClubMemberRequest;
import com.happy.friendogly.club.dto.response.FindClubByFilterResponse;
import com.happy.friendogly.club.dto.response.FindClubOwningResponse;
import com.happy.friendogly.club.dto.response.FindClubParticipatingResponse;
import com.happy.friendogly.club.dto.response.FindClubResponse;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class ClubQueryServiceTest extends ClubServiceTest {

    @Autowired
    private ClubQueryService clubQueryService;

    @Autowired
    private ClubCommandService clubCommandService;

    private Member savedMember;
    private Pet savedPet;

    @BeforeEach
    void setMemberAndPet() {
        savedMember = createSavedMember();
        savedPet = createSavedPet(savedMember);
    }

    @DisplayName("필터링된 모임을 정보를 조회한다.")
    @Test
    void findSearching() {
        Club club1 = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        Club club2 = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL, SizeType.MEDIUM)
        );

        FindClubByFilterRequest request = new FindClubByFilterRequest(
                FilterCondition.ALL.name(),
                province,
                city,
                null,
                Set.of(Gender.FEMALE.name()),
                Set.of(SizeType.SMALL.name())
        );

        List<FindClubByFilterResponse> responses = clubQueryService.findByFilter(savedMember.getId(), request);
        List<FindClubByFilterResponse> expectedResponses = List.of(
                new FindClubByFilterResponse(club2, List.of(petImageUrl)),
                new FindClubByFilterResponse(club1, List.of(petImageUrl))
        );

        FindClubByFilterResponse actual1 = responses.get(0);
        FindClubByFilterResponse expected1 = expectedResponses.get(0);
        FindClubByFilterResponse actual2 = responses.get(1);
        FindClubByFilterResponse expected2 = expectedResponses.get(1);

        assertAll(
                () -> assertThat(actual1.id()).isEqualTo(expected1.id()),
                () -> assertThat(actual1.title()).isEqualTo(expected1.title()),
                () -> assertThat(actual1.content()).isEqualTo(expected1.content()),
                () -> assertThat(actual1.address()).isEqualTo(expected1.address()),
                () -> assertThat(actual1.ownerMemberName()).isEqualTo(expected1.ownerMemberName()),
                () -> assertThat(actual1.status()).isEqualTo(expected1.status()),
                () -> assertThat(actual1.allowedSize()).containsExactlyInAnyOrderElementsOf(expected1.allowedSize()),
                () -> assertThat(actual1.allowedGender()).containsExactlyInAnyOrderElementsOf(
                        expected1.allowedGender()),
                () -> assertThat(actual1.memberCapacity()).isEqualTo(expected1.memberCapacity()),
                () -> assertThat(actual1.currentMemberCount()).isEqualTo(expected1.currentMemberCount()),
                () -> assertThat(actual1.petImageUrls()).containsExactlyInAnyOrderElementsOf(expected1.petImageUrls()),
                () -> assertThat(actual2.id()).isEqualTo(expected2.id()),
                () -> assertThat(actual2.title()).isEqualTo(expected2.title()),
                () -> assertThat(actual2.content()).isEqualTo(expected2.content()),
                () -> assertThat(actual2.address()).isEqualTo(expected2.address()),
                () -> assertThat(actual2.ownerMemberName()).isEqualTo(expected2.ownerMemberName()),
                () -> assertThat(actual2.status()).isEqualTo(expected2.status()),
                () -> assertThat(actual2.allowedSize()).containsExactlyInAnyOrderElementsOf(expected2.allowedSize()),
                () -> assertThat(actual2.allowedGender()).containsExactlyInAnyOrderElementsOf(
                        expected2.allowedGender()),
                () -> assertThat(actual2.memberCapacity()).isEqualTo(expected2.memberCapacity()),
                () -> assertThat(actual2.currentMemberCount()).isEqualTo(expected2.currentMemberCount()),
                () -> assertThat(actual2.petImageUrls()).containsExactlyInAnyOrderElementsOf(expected2.petImageUrls())
        );
    }

    @DisplayName("필터링된 모임을 정보가 없으면 빈 리스트를 반환한다.")
    @Transactional
    @Test
    void findSearching_Nothing() {
        Club club = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        FindClubByFilterRequest request = new FindClubByFilterRequest(
                FilterCondition.ALL.name(),
                province,
                city,
                village,
                Set.of(Gender.MALE.name()),
                Set.of(SizeType.SMALL.name())
        );

        List<FindClubByFilterResponse> responses = clubQueryService.findByFilter(savedMember.getId(), request);

        assertThat(responses.isEmpty()).isTrue();
    }

    @DisplayName("내가 방장인 모임을 조회한다.")
    @Transactional
    @Test
    void findMine() {
        Club club1 = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        Member savedMember2 = memberRepository.save(Member.builder()
                .name("위브")
                .build());
        Pet savedPet2 = createSavedPet(savedMember2);
        Pet savedPet3 = createSavedPet(savedMember2);
        Club club2 = createSavedClub(
                savedMember2,
                List.of(savedPet2, savedPet3),
                Set.of(Gender.FEMALE),
                Set.of(SizeType.SMALL)
        );
        club2.addClubMember(savedMember);
        club2.addClubPet(List.of(savedPet));

        List<FindClubOwningResponse> actual = clubQueryService.findOwning(savedMember.getId());
        List<FindClubOwningResponse> expected = List.of(new FindClubOwningResponse(club1, List.of(petImageUrl)));

        FindClubOwningResponse actual1 = actual.get(0);
        FindClubOwningResponse expected1 = expected.get(0);

        assertAll(
                () -> assertThat(actual1.id()).isEqualTo(expected1.id()),
                () -> assertThat(actual1.title()).isEqualTo(expected1.title()),
                () -> assertThat(actual1.content()).isEqualTo(expected1.content()),
                () -> assertThat(actual1.address()).isEqualTo(expected1.address()),
                () -> assertThat(actual1.ownerMemberName()).isEqualTo(expected1.ownerMemberName()),
                () -> assertThat(actual1.status()).isEqualTo(expected1.status()),
                () -> assertThat(actual1.allowedSize()).containsExactlyInAnyOrderElementsOf(expected1.allowedSize()),
                () -> assertThat(actual1.allowedGender()).containsExactlyInAnyOrderElementsOf(
                        expected1.allowedGender()),
                () -> assertThat(actual1.memberCapacity()).isEqualTo(expected1.memberCapacity()),
                () -> assertThat(actual1.currentMemberCount()).isEqualTo(expected1.currentMemberCount()),
                () -> assertThat(actual1.petImageUrls()).containsExactlyInAnyOrderElementsOf(expected1.petImageUrls())
        );
    }

    @DisplayName("내가 참여중인 모임을 조회한다.")
    @Transactional
    @Test
    void findParticipating() {
        Club club1 = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        Member savedMember2 = memberRepository.save(Member.builder()
                .name("위브")
                .build());
        Pet savedPet2 = createSavedPet(savedMember2);
        Pet savedPet3 = createSavedPet(savedMember2);
        Club club2 = createSavedClub(
                savedMember2,
                List.of(savedPet2, savedPet3),
                Set.of(Gender.FEMALE),
                Set.of(SizeType.SMALL)
        );
        clubCommandService.joinClub(
                club2.getId(),
                savedMember.getId(),
                new SaveClubMemberRequest(List.of(savedPet.getId()))
        );

        List<FindClubParticipatingResponse> actual = clubQueryService.findParticipating(savedMember.getId());
        List<FindClubParticipatingResponse> expected = List.of(
                new FindClubParticipatingResponse(club2, List.of(petImageUrl, petImageUrl)),
                new FindClubParticipatingResponse(club1, List.of(petImageUrl))
        );

        FindClubParticipatingResponse actual1 = actual.get(0);
        FindClubParticipatingResponse actual2 = actual.get(1);
        FindClubParticipatingResponse expected1 = expected.get(0);
        FindClubParticipatingResponse expected2 = expected.get(1);

        assertAll(
                () -> assertThat(actual1.id()).isEqualTo(expected1.id()),
                () -> assertThat(actual1.title()).isEqualTo(expected1.title()),
                () -> assertThat(actual1.content()).isEqualTo(expected1.content()),
                () -> assertThat(actual1.address()).isEqualTo(expected1.address()),
                () -> assertThat(actual1.ownerMemberName()).isEqualTo(expected1.ownerMemberName()),
                () -> assertThat(actual1.status()).isEqualTo(expected1.status()),
                () -> assertThat(actual1.allowedSize()).containsExactlyInAnyOrderElementsOf(expected1.allowedSize()),
                () -> assertThat(actual1.allowedGender()).containsExactlyInAnyOrderElementsOf(
                        expected1.allowedGender()),
                () -> assertThat(actual1.memberCapacity()).isEqualTo(expected1.memberCapacity()),
                () -> assertThat(actual1.currentMemberCount()).isEqualTo(expected1.currentMemberCount()),
                () -> assertThat(actual1.petImageUrls()).containsExactlyInAnyOrderElementsOf(expected1.petImageUrls()),
                () -> assertThat(actual2.id()).isEqualTo(expected2.id()),
                () -> assertThat(actual2.title()).isEqualTo(expected2.title()),
                () -> assertThat(actual2.content()).isEqualTo(expected2.content()),
                () -> assertThat(actual2.address()).isEqualTo(expected2.address()),
                () -> assertThat(actual2.ownerMemberName()).isEqualTo(expected2.ownerMemberName()),
                () -> assertThat(actual2.status()).isEqualTo(expected2.status()),
                () -> assertThat(actual2.allowedSize()).containsExactlyInAnyOrderElementsOf(expected2.allowedSize()),
                () -> assertThat(actual2.allowedGender()).containsExactlyInAnyOrderElementsOf(
                        expected2.allowedGender()),
                () -> assertThat(actual2.memberCapacity()).isEqualTo(expected2.memberCapacity()),
                () -> assertThat(actual2.currentMemberCount()).isEqualTo(expected2.currentMemberCount()),
                () -> assertThat(actual2.petImageUrls()).containsExactlyInAnyOrderElementsOf(expected2.petImageUrls())
        );
    }

    @DisplayName("단건 조회")
    @Test
    void findById() {
        Club club = createSavedClub(
                savedMember,
                List.of(savedPet),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        FindClubResponse response = clubQueryService.findById(savedMember.getId(), club.getId());

        assertThat(response.id()).isEqualTo(club.getId());
    }
}
