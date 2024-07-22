package com.woowacourse.friendogly.club.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.dto.response.FindSearchingClubResponse;
import com.woowacourse.friendogly.pet.domain.Gender;
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
        //서울특별시 송파구 신청동, (암컷, 중성화 암컷), 크기는 소형견이 조건인 club
        Club club = saveNewClub();

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
}
