package com.woowacourse.friendogly.footprint.service;

import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.footprint.domain.Location;
import com.woowacourse.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.response.FindMyLatestFootprintTimeResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FootprintQueryService {

    private static final int FOOTPRINT_DURATION_HOURS = 24;

    private final FootprintRepository footprintRepository;

    public FootprintQueryService(FootprintRepository footprintRepository) {
        this.footprintRepository = footprintRepository;
    }

    public List<FindNearFootprintResponse> findNear(Long memberId, FindNearFootprintRequest request) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(FOOTPRINT_DURATION_HOURS);
        List<Footprint> recentFootprints = footprintRepository.findByCreatedAtAfter(startTime);

        Location currentLocation = new Location(request.latitude(), request.longitude());

        return recentFootprints.stream()
            .filter(footprint -> footprint.isNear(currentLocation))
            .map(footprint -> new FindNearFootprintResponse(footprint, footprint.isCreatedBy(memberId)))
            .toList();
    }

    public FindMyLatestFootprintTimeResponse findMyLatestFootprintTime(Long memberId) {
        return footprintRepository.findTopOneByMemberIdOrderByCreatedAtDesc(memberId)
            .map(footprint -> new FindMyLatestFootprintTimeResponse(footprint.getCreatedAt()))
            .orElse(new FindMyLatestFootprintTimeResponse(null));
    }
}
