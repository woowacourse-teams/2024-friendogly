package com.woowacourse.friendogly.club.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.dto.response.FindSearchingClubResponse;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class ClubQueryServiceTest extends ClubServiceTest {

    @Autowired
    private ClubQueryService clubQueryService;

    @DisplayName("필터링된 모임을 정보를 조회한다.")
    @Test
    void findSearching() {
        Member savedMember = createSavedMember();
        Pet savedPet = createSavedPet(savedMember);
        Club club = createSavedClub(savedMember, savedPet, Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL));

        FindSearchingClubRequest request = new FindSearchingClubRequest(
                address,
                Set.of(Gender.FEMALE),
                Set.of(SizeType.SMALL)
        );

        List<FindSearchingClubResponse> responses = clubQueryService.findSearching(request);
        List<FindSearchingClubResponse> expectedResponses = List.of(
                new FindSearchingClubResponse(club, 1, List.of(petImageUrl))
        );

        FindSearchingClubResponse actual = responses.get(0);
        FindSearchingClubResponse expected = expectedResponses.get(0);

        assertAll(
                () -> assertThat(actual.id()).isEqualTo(expected.id()),
                () -> assertThat(actual.title()).isEqualTo(expected.title()),
                () -> assertThat(actual.content()).isEqualTo(expected.content()),
                () -> assertThat(actual.address()).isEqualTo(expected.address()),
                () -> assertThat(actual.ownerMemberName()).isEqualTo(expected.ownerMemberName()),
                () -> assertThat(actual.status()).isEqualTo(expected.status()),
                () -> assertThat(actual.allowedSize()).containsExactlyInAnyOrderElementsOf(expected.allowedSize()),
                () -> assertThat(actual.allowedGender()).containsExactlyInAnyOrderElementsOf(expected.allowedGender()),
                () -> assertThat(actual.memberCapacity()).isEqualTo(expected.memberCapacity()),
                () -> assertThat(actual.currentMemberCount()).isEqualTo(expected.currentMemberCount()),
                () -> assertThat(actual.petImageUrls()).containsExactlyInAnyOrderElementsOf(expected.petImageUrls())
        );
    }

    @DisplayName("필터링된 모임을 정보가 없으면 빈 리스트를 반환한다.")
    @Test
    void findSearching_Nothing() {
        Member savedMember = createSavedMember();
        Pet savedPet = createSavedPet(savedMember);
        Club club = createSavedClub(savedMember, savedPet, Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL));

        FindSearchingClubRequest request = new FindSearchingClubRequest(
                address,
                Set.of(Gender.MALE),
                Set.of(SizeType.SMALL)
        );

        List<FindSearchingClubResponse> responses = clubQueryService.findSearching(request);

        assertThat(responses.isEmpty()).isTrue();
    }
}
