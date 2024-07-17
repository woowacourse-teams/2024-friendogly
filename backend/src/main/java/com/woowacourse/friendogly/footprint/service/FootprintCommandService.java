package com.woowacourse.friendogly.footprint.service;

import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.footprint.domain.Location;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FootprintCommandService {

    private final FootprintRepository footprintRepository;
    private final MemberRepository memberRepository;

    public Long save(SaveFootprintRequest request) {
        Member member = memberRepository.findById(request.memberId())
            .orElseThrow(() -> new IllegalArgumentException("멤버 없음"));

        Footprint footprint = footprintRepository.save(
            Footprint.builder()
                .member(member)
                .location(new Location(request.latitude(), request.longitude()))
                .build()
        );

        return footprint.getId();
    }
}
