package com.woowacourse.friendogly.footprint.service;

import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.footprint.domain.Location;
import com.woowacourse.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FootprintQueryService {

    private static final int FOOTPRINT_REMAIN_HOURS = 24;

    private final FootprintRepository footprintRepository;

    public List<FindNearFootprintResponse> findNear(FindNearFootprintRequest request) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(FOOTPRINT_REMAIN_HOURS);
        List<Footprint> recentFootprints = footprintRepository.findByCreatedAtAfter(startTime);

        Location currentLocation = new Location(request.latitude(), request.longitude());

        return recentFootprints.stream()
            .filter(footprint -> footprint.isNear(currentLocation))
            .map(FindNearFootprintResponse::from)
            .toList();
    }
}
