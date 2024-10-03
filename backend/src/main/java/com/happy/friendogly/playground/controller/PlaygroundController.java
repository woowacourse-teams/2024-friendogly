package com.happy.friendogly.playground.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.common.ApiResponse;
import com.happy.friendogly.playground.dto.request.SavePlaygroundRequest;
import com.happy.friendogly.playground.dto.request.UpdatePlaygroundArrivalRequest;
import com.happy.friendogly.playground.dto.response.FindPlaygroundDetailResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundLocationResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundSummaryResponse;
import com.happy.friendogly.playground.dto.response.SavePlaygroundResponse;
import com.happy.friendogly.playground.dto.response.UpdatePlaygroundArrivalResponse;
import com.happy.friendogly.playground.service.PlaygroundCommandService;
import com.happy.friendogly.playground.service.PlaygroundQueryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
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
        SavePlaygroundResponse savePlaygroundResponse = new SavePlaygroundResponse(
                1L,
                request.latitude(),
                request.longitude()
        );
        return ResponseEntity.created(URI.create("/playgrounds/" + 1))
                .body(ApiResponse.ofSuccess(savePlaygroundResponse));
    }

    @GetMapping("/{playgroundId}")
    public ApiResponse<FindPlaygroundDetailResponse> findById(@Auth Long memberId, @PathVariable Long playgroundId) {
        FindPlaygroundDetailResponse response = playgroundQueryService.findDetail(memberId, playgroundId);
        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/{id}/summary")
    public ApiResponse<FindPlaygroundSummaryResponse> findSummaryById(@PathVariable Long id) {
        return ApiResponse.ofSuccess(new FindPlaygroundSummaryResponse(1L, 10, 4));
    }

    @GetMapping("/locations")
    public ApiResponse<List<FindPlaygroundLocationResponse>> findAllLocation(@Auth Long memberId) {
        return ApiResponse.ofSuccess(
                List.of(
                        new FindPlaygroundLocationResponse(1L, 37.5173316, 127.1011661, true),
                        new FindPlaygroundLocationResponse(2L, 37.5185122, 127.098778, false),
                        new FindPlaygroundLocationResponse(3L, 37.5136533, 127.0983182, false),
                        new FindPlaygroundLocationResponse(4L, 37.5131474, 127.1042528, false)
                )
        );
    }

    @PatchMapping("/arrival")
    public ApiResponse<UpdatePlaygroundArrivalResponse> updateArrival(
            @Auth Long memberId,
            @Valid @RequestBody UpdatePlaygroundArrivalRequest request
    ) {
        return ApiResponse.ofSuccess(new UpdatePlaygroundArrivalResponse(true));
    }
}
