package com.happy.friendogly.footprint.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.common.ApiResponse;
import com.happy.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.happy.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.happy.friendogly.footprint.dto.request.UpdateWalkStatusAutoRequest;
import com.happy.friendogly.footprint.dto.response.FindMyLatestFootprintTimeAndPetExistenceResponse;
import com.happy.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.happy.friendogly.footprint.dto.response.FindOneFootprintResponse;
import com.happy.friendogly.footprint.dto.response.SaveFootprintResponse;
import com.happy.friendogly.footprint.dto.response.UpdateWalkStatusAutoResponse;
import com.happy.friendogly.footprint.dto.response.UpdateWalkStatusManualResponse;
import com.happy.friendogly.footprint.service.FootprintCommandService;
import com.happy.friendogly.footprint.service.FootprintQueryService;
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
        FindOneFootprintResponse response = footprintQueryService.findOne(memberId, footprintId);
        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/near")
    public ApiResponse<List<FindNearFootprintResponse>> findNear(
            @Auth Long memberId,
            @Valid FindNearFootprintRequest request
    ) {
        List<FindNearFootprintResponse> response = footprintQueryService.findNear(memberId, request);
        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/mine/latest")
    public ApiResponse<FindMyLatestFootprintTimeAndPetExistenceResponse> findMyLatestFootprintTimeAndPetExistence(
            @Auth Long memberId
    ) {
        FindMyLatestFootprintTimeAndPetExistenceResponse response
                = footprintQueryService.findMyLatestFootprintTimeAndPetExistence(memberId);
        return ApiResponse.ofSuccess(response);
    }

    @PatchMapping("/recent/walk-status/auto")
    public ApiResponse<UpdateWalkStatusAutoResponse> updateWalkStatusAuto(
            @Auth Long memberId,
            @Valid @RequestBody UpdateWalkStatusAutoRequest request
    ) {
        UpdateWalkStatusAutoResponse response = footprintCommandService.updateWalkStatusAuto(memberId, request);
        return ApiResponse.ofSuccess(response);
    }

    @PatchMapping("/recent/walk-status/manual")
    public ApiResponse<UpdateWalkStatusManualResponse> updateWalkStatusManual(
            @Auth Long memberId
    ) {
        UpdateWalkStatusManualResponse response = footprintCommandService.updateWalkStatusManual(memberId);
        return ApiResponse.ofSuccess(response);
    }

    @DeleteMapping("/{footprintId}")
    public ResponseEntity<Void> delete(
            @Auth Long memberId,
            @PathVariable Long footprintId
    ) {
        footprintCommandService.delete(memberId, footprintId);
        return ResponseEntity.noContent().build();
    }
}
