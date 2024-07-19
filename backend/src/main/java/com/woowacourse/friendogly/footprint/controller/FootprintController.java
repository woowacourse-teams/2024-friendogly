package com.woowacourse.friendogly.footprint.controller;

import com.woowacourse.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.woowacourse.friendogly.footprint.service.FootprintCommandService;
import com.woowacourse.friendogly.footprint.service.FootprintQueryService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<Void> save(@RequestBody SaveFootprintRequest request) {
        Long id = footprintCommandService.save(request);
        return ResponseEntity.created(URI.create("/footprints/" + id))
            .build();
    }

    @GetMapping("/near")
    public List<FindNearFootprintResponse> findNear(FindNearFootprintRequest request) {
        return footprintQueryService.findNear(request);
    }
}
