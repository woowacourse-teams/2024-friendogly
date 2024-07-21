package com.woowacourse.friendogly.club.controller;

import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.dto.response.FindSearchingClubResponse;
import com.woowacourse.friendogly.club.service.ClubCommandService;
import com.woowacourse.friendogly.club.service.ClubQueryService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubCommandService clubCommandService;
    private final ClubQueryService clubQueryService;

    public ClubController(ClubCommandService clubCommandService, ClubQueryService clubQueryService) {
        this.clubCommandService = clubCommandService;
        this.clubQueryService = clubQueryService;
    }


    @GetMapping("/searching")
    public List<FindSearchingClubResponse> findSearching(@RequestBody FindSearchingClubRequest request) {
        return clubQueryService.findSearching(request);
    }
}
