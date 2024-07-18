package com.woowacourse.friendogly.footprint.controller;

import com.woowacourse.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.request.UpdateFootprintImageRequest;
import com.woowacourse.friendogly.footprint.dto.response.FindMyLatestFootprintTimeResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.woowacourse.friendogly.footprint.dto.response.UpdateFootprintImageResponse;
import com.woowacourse.friendogly.footprint.service.FootprintCommandService;
import com.woowacourse.friendogly.footprint.service.FootprintQueryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/footprints")
public class FootprintController {

    private final FootprintCommandService footprintCommandService;
    private final FootprintQueryService footprintQueryService;

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody SaveFootprintRequest request) {
        Long id = footprintCommandService.save(request);
        return ResponseEntity.created(URI.create("/footprints/" + id))
            .build();
    }

    @GetMapping("/near")
    public ResponseEntity<List<FindNearFootprintResponse>> findNear(@Valid FindNearFootprintRequest request) {
        // memberId == 1L 로 dummy data 사용
        // TODO: 추후 토큰에서 memberId를 가져오도록 변경
        List<FindNearFootprintResponse> response = footprintQueryService.findNear(1L, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mine/latest")
    public ResponseEntity<FindMyLatestFootprintTimeResponse> findMyLatestFootprintTime() {
        // memberId == 1L 로 dummy data 사용
        // TODO: 추후 토큰에서 memberId를 가져오도록 변경
        FindMyLatestFootprintTimeResponse response = footprintQueryService.findMyLatestFootprintTime(1L);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/image/{footprintId}")
    public ResponseEntity<UpdateFootprintImageResponse> updateFootprintImage(
        @PathVariable Long footprintId,
        @ModelAttribute UpdateFootprintImageRequest request
    ) {
        UpdateFootprintImageResponse response = footprintCommandService.updateFootprintImage(footprintId, request);
        return ResponseEntity.ok(response);
    }
}
