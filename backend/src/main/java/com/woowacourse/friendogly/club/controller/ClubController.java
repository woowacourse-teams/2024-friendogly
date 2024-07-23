package com.woowacourse.friendogly.club.controller;

import com.woowacourse.friendogly.auth.Auth;
import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.FindSearchingClubResponse;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.club.service.ClubCommandService;
import com.woowacourse.friendogly.club.service.ClubQueryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<List<FindSearchingClubResponse>> findSearching(@Valid FindSearchingClubRequest request) {
        return ResponseEntity.ok(clubQueryService.findSearching(request));
    }

    @PostMapping
    public ResponseEntity<SaveClubResponse> save(
            @Auth Long memberId,
            @Valid @RequestBody SaveClubRequest request) {
        SaveClubResponse response = clubCommandService.save(memberId, request);
        return ResponseEntity.created(URI.create("/clubs" + response.id())).body(response);
    }
}
