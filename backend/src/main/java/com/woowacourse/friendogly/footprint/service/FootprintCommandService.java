package com.woowacourse.friendogly.footprint.service;

import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.footprint.domain.Location;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.request.UpdateFootprintImageRequest;
import com.woowacourse.friendogly.footprint.dto.response.UpdateFootprintImageResponse;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    public UpdateFootprintImageResponse updateFootprintImage(Long footprintId, UpdateFootprintImageRequest request) {
        Footprint footprint = footprintRepository.findById(footprintId)
            .orElseThrow(() -> new FriendoglyException("존재하지 않는 Footprint ID입니다."));

        MultipartFile multipartFile = request.imageFile();
        // TODO: 더미 데이터 URL입니다. 나중에 이미지 저장소(AWS S3)와 연동 필요 !!!
        // TODO: 연동 완료되면 테스트도 작성 필요합니다.
        String imageUrl = "https://img.extmovie.com/files/attach/images/148/921/189/074/6a71e831234e113e3ed215405098109c.jpg";
        footprint.setImageUrl(imageUrl);
        return new UpdateFootprintImageResponse(imageUrl);
    }
}
