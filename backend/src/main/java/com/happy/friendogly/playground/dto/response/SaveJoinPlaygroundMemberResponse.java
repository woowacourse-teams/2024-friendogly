package com.happy.friendogly.playground.dto.response;

import com.happy.friendogly.playground.domain.PlaygroundMember;
import java.time.LocalDateTime;

public record SaveJoinPlaygroundMemberResponse(
        Long playgroundId,
        Long memberId,
        String message,
        boolean isArrived,
        LocalDateTime exitTime
) {
    public static SaveJoinPlaygroundMemberResponse from(PlaygroundMember playgroundMember) {
        return new SaveJoinPlaygroundMemberResponse(
                playgroundMember.getPlayground().getId(),
                playgroundMember.getMember().getId(),
                playgroundMember.getMessage(),
                playgroundMember.isInside(),
                playgroundMember.getExitTime()
        );
    }
}


