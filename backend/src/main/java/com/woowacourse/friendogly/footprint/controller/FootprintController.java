package com.woowacourse.friendogly.footprint.controller;

import com.woowacourse.friendogly.auth.Auth;
import com.woowacourse.friendogly.common.ApiResponse;
import com.woowacourse.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.response.FindMyLatestFootprintTimeAndPetExistenceResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindOneFootprintResponse;
import com.woowacourse.friendogly.footprint.dto.response.SaveFootprintResponse;
import com.woowacourse.friendogly.footprint.service.FootprintCommandService;
import com.woowacourse.friendogly.footprint.service.FootprintQueryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/footprints")
public class FootprintController {

    private final FootprintCommandService footprintCommandService;
    private final FootprintQueryService footprintQueryService;

    public FootprintController(
            FootprintCommandService footprintCommandService,
            FootprintQueryService footprintQueryService
    ) {
        this.footprintCommandService = footprintCommandService;
        this.footprintQueryService = footprintQueryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SaveFootprintResponse>> save(
            @Auth Long memberId,
            @Valid @RequestBody SaveFootprintRequest request
    ) {
        SaveFootprintResponse response = footprintCommandService.save(memberId, request);
        return ResponseEntity.created(URI.create("/footprints/" + response.id()))
                .body(ApiResponse.ofSuccess(response));
    }

    @GetMapping("/{footprintId}")
    public ApiResponse<FindOneFootprintResponse> findOne(
            @Auth Long memberId,
            @PathVariable Long footprintId
    ) {
        // TODO: 추후 토큰에서 memberId를 가져오도록 변경
        FindOneFootprintResponse response = footprintQueryService.findOne(memberId, footprintId);
        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/near")
    public ApiResponse<List<FindNearFootprintResponse>> findNear(
            @Auth Long memberId,
            @Valid FindNearFootprintRequest request
    ) {
        // TODO: 추후 토큰에서 memberId를 가져오도록 변경
        List<FindNearFootprintResponse> response = footprintQueryService.findNear(memberId, request);
        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/mine/latest")
    public ApiResponse<FindMyLatestFootprintTimeAndPetExistenceResponse> findMyLatestFootprintTimeAndPetExistence(
            @Auth Long memberId
    ) {
        // TODO: 추후 토큰에서 memberId를 가져오도록 변경
        FindMyLatestFootprintTimeAndPetExistenceResponse response
                = footprintQueryService.findMyLatestFootprintTimeAndPetExistence(memberId);
        return ApiResponse.ofSuccess(response);
    }
}
