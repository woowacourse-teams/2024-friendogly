package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.chat.domain.PrivateChatRoom;
import com.woowacourse.friendogly.chat.dto.response.FindMyPrivateChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.PrivateChatRoomRepository;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PrivateChatRoomQueryService {

    private final PrivateChatRoomRepository privateChatRoomRepository;
    private final MemberRepository memberRepository;

    public PrivateChatRoomQueryService(
            PrivateChatRoomRepository privateChatRoomRepository,
            MemberRepository memberRepository
    ) {
        this.privateChatRoomRepository = privateChatRoomRepository;
        this.memberRepository = memberRepository;
    }

    public List<FindMyPrivateChatRoomResponse> findMine(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FriendoglyException(memberId + "는 존재하지 않는 Member ID입니다."));

        List<PrivateChatRoom> response = privateChatRoomRepository.findByMemberOrOtherMember(member, member);

        return response.stream()
                .map(FindMyPrivateChatRoomResponse::new)
                .toList();
    }
}
