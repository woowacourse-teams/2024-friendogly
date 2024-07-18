package com.woowacourse.friendogly.footprint.service;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.footprint.domain.Location;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FootprintCommandService {

    private static final int FOOTPRINT_COOLDOWN = 30;

    private final FootprintRepository footprintRepository;
    private final MemberRepository memberRepository;

    public Long save(SaveFootprintRequest request) {
        Member member = memberRepository.findById(request.memberId())
            .orElseThrow(() -> new FriendoglyException("존재하지 않는 사용자 ID입니다."));

        validateRecentFootprintExists(request.memberId());

        Footprint footprint = footprintRepository.save(
            Footprint.builder()
                .member(member)
                .location(new Location(request.latitude(), request.longitude()))
                .build()
        );

        return footprint.getId();
    }

    private void validateRecentFootprintExists(Long memberId) {
        boolean exists = footprintRepository.existsByMemberIdAndCreatedAtAfter(
            memberId,
            LocalDateTime.now().minusSeconds(FootprintCommandService.FOOTPRINT_COOLDOWN)
        );

        if (exists) {
            throw new FriendoglyException("마지막 발자국을 찍은 뒤 30초가 경과되지 않았습니다.");
        }
    }
}
