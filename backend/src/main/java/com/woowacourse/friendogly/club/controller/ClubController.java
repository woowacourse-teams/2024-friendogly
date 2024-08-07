package com.woowacourse.friendogly.club.controller;

import com.woowacourse.friendogly.auth.Auth;
import com.woowacourse.friendogly.club.dto.request.FindClubByFilterRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubMemberRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.FindClubByFilterResponse;
import com.woowacourse.friendogly.club.dto.response.FindClubResponse;
import com.woowacourse.friendogly.club.dto.response.SaveClubMemberResponse;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.club.service.ClubCommandService;
import com.woowacourse.friendogly.club.service.ClubQueryService;
import com.woowacourse.friendogly.common.ApiResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubCommandService clubCommandService;
    private final ClubQueryService clubQueryService;

    public ClubController(ClubCommandService clubCommandService, ClubQueryService clubQueryService) {
        this.clubCommandService = clubCommandService;
        this.clubQueryService = clubQueryService;
    }

    @GetMapping("/{id}")
    public ApiResponse<FindClubResponse> findById(@Auth Long memberId, @PathVariable Long id) {
        return ApiResponse.ofSuccess(clubQueryService.findById(memberId, id));
    }

    @GetMapping("/searching")
    public ApiResponse<List<FindClubByFilterResponse>> findByFilter(
            @Auth Long memberId,
            @Valid @ModelAttribute FindClubByFilterRequest request
    ) {
        return ApiResponse.ofSuccess(clubQueryService.findFindByFilter(memberId, request));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SaveClubResponse>> save(
            @Auth Long memberId,
            @Valid @RequestPart SaveClubRequest request,
            @RequestPart(required = false) MultipartFile image
    ) {
        SaveClubResponse response = clubCommandService.save(memberId, image, request);
        return ResponseEntity.created(URI.create("/clubs" + response.id())).body(ApiResponse.ofSuccess(response));
    }

    @PostMapping("/{clubId}/members")
    public ResponseEntity<ApiResponse<SaveClubMemberResponse>> joinClub(
            @PathVariable Long clubId,
            @Auth Long memberId,
            @Valid @RequestBody SaveClubMemberRequest request
    ) {
        SaveClubMemberResponse response = clubCommandService.joinClub(clubId, memberId, request);
        return ResponseEntity.created(URI.create("/clubs/" + clubId + "/members/" + response.memberId()))
                .body(ApiResponse.ofSuccess(response));
    }

    @DeleteMapping("/{clubId}/members")
    public ResponseEntity<Void> deleteClubMember(
            @Auth Long memberId,
            @PathVariable Long clubId
    ) {
        clubCommandService.deleteClubMember(clubId, memberId);
        return ResponseEntity.noContent().build();
    }

}
