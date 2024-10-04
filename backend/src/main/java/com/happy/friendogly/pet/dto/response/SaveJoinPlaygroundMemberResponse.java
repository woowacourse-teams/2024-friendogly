package com.happy.friendogly.pet.dto.response;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import java.time.LocalDateTime;

public record SaveJoinPlaygroundMemberResponse(
        Playground playground,
        Member member,
        String message,
        boolean isInside,
        LocalDateTime exitTime
) {
    public static SaveJoinPlaygroundMemberResponse from(PlaygroundMember playgroundMember) {
        return new SaveJoinPlaygroundMemberResponse(
                playgroundMember.getPlayground(),
                playgroundMember.getMember(),
                playgroundMember.getMessage(),
                playgroundMember.isInside(),
                playgroundMember.getExitTime()
        );
    }
}


