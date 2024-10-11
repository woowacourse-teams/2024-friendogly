package com.happy.friendogly.playground.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.common.ApiResponse;
import com.happy.friendogly.playground.dto.request.SavePlaygroundRequest;
import com.happy.friendogly.playground.dto.request.UpdatePlaygroundArrivalRequest;
import com.happy.friendogly.playground.dto.request.UpdatePlaygroundMemberMessageRequest;
import com.happy.friendogly.playground.dto.response.FindPlaygroundDetailResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundLocationResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundSummaryResponse;
import com.happy.friendogly.playground.dto.response.SaveJoinPlaygroundMemberResponse;
import com.happy.friendogly.playground.dto.response.SavePlaygroundResponse;
import com.happy.friendogly.playground.dto.response.UpdatePlaygroundArrivalResponse;
import com.happy.friendogly.playground.dto.response.UpdatePlaygroundMemberMessageResponse;
import com.happy.friendogly.playground.service.PlaygroundCommandService;
import com.happy.friendogly.playground.service.PlaygroundQueryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/playgrounds")
public class PlaygroundController {

    private final PlaygroundCommandService playgroundCommandService;
    private final PlaygroundQueryService playgroundQueryService;

    public PlaygroundController(
            PlaygroundCommandService playgroundCommandService,
            PlaygroundQueryService playgroundQueryService
    ) {
        this.playgroundCommandService = playgroundCommandService;
        this.playgroundQueryService = playgroundQueryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SavePlaygroundResponse>> save(
            @Auth Long memberId,
            @Valid @RequestBody SavePlaygroundRequest request
    ) {
        SavePlaygroundResponse response = playgroundCommandService.save(request, memberId);
        return ResponseEntity.created(URI.create("/playgrounds/" + response.id()))
                .body(ApiResponse.ofSuccess(response));
    }

    @GetMapping("/{playgroundId}")
    public ApiResponse<FindPlaygroundDetailResponse> findById(@Auth Long memberId, @PathVariable Long playgroundId) {
        FindPlaygroundDetailResponse response = playgroundQueryService.findDetail(memberId, playgroundId);
        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/{playgroundId}/summary")
    public ApiResponse<FindPlaygroundSummaryResponse> findSummaryById(@PathVariable Long playgroundId) {
        return ApiResponse.ofSuccess(playgroundQueryService.findSummary(playgroundId));
    }

    @GetMapping("/locations")
    public ApiResponse<List<FindPlaygroundLocationResponse>> findAllLocation(@Auth Long memberId) {
        return ApiResponse.ofSuccess(playgroundQueryService.findLocations(memberId));
    }

    @PatchMapping("/arrival")
    public ApiResponse<UpdatePlaygroundArrivalResponse> updateArrival(
            @Auth Long memberId,
            @Valid @RequestBody UpdatePlaygroundArrivalRequest request
    ) {
        return ApiResponse.ofSuccess(playgroundCommandService.updateArrival(request, memberId));
    }

    @PostMapping("/{playgroundId}/join")
    public ApiResponse<SaveJoinPlaygroundMemberResponse> saveJoinMember(
            @Auth Long memberId,
            @PathVariable Long playgroundId
    ) {
        SaveJoinPlaygroundMemberResponse response = playgroundCommandService
                .joinPlayground(memberId, playgroundId);
        return ApiResponse.ofSuccess(response);
    }

    @DeleteMapping("/leave")
    public ResponseEntity<Void> deleteJoinMember(@Auth Long memberId) {
        playgroundCommandService.leavePlayground(memberId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/message")
    public ApiResponse<UpdatePlaygroundMemberMessageResponse> updateMessage(
            @Auth Long memberId,
            UpdatePlaygroundMemberMessageRequest request
    ) {
        UpdatePlaygroundMemberMessageResponse response = playgroundCommandService.updateMemberMessage(
                request, memberId
        );
        return ApiResponse.ofSuccess(response);
    }
}
