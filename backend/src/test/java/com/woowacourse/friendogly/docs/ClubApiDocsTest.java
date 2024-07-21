package com.woowacourse.friendogly.docs;

import com.woowacourse.friendogly.club.controller.ClubController;
import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.service.ClubCommandService;
import com.woowacourse.friendogly.club.service.ClubQueryService;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(ClubController.class)
public class ClubApiDocsTest extends RestDocsTest {

    @MockBean
    private ClubCommandService clubCommandService;

    @MockBean
    private ClubQueryService clubQueryService;

    @DisplayName("필터링 조건을 통해 모임 리스트를 조회한다.")
    @Test
    void findSearching() {
        FindSearchingClubRequest request = new FindSearchingClubRequest(
                "서울특별시 송파구 신청동 잠실 6동",
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );
    }

    @Override
    protected Object controller() {
        return new ClubController(clubCommandService, clubQueryService);
    }
}
