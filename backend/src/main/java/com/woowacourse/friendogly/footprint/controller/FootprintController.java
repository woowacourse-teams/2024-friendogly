package com.woowacourse.friendogly.footprint.controller;

import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.service.FootprintCommandService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/footprints")
public class FootprintController {

    private final FootprintCommandService footprintCommandService;

    @PostMapping
    public ResponseEntity<Void> save(SaveFootprintRequest request) {
        Long id = footprintCommandService.save(request);
        return ResponseEntity.created(URI.create("/footprints/" + id))
            .build();
    }
}
