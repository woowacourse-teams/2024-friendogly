package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.chat.domain.PrivateChatRoom;
import com.woowacourse.friendogly.chat.dto.response.InvitePrivateChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.PrivateChatRoomRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrivateChatRoomCommandService {

    private final PrivateChatRoomRepository privateChatRoomRepository;
    private final MemberRepository memberRepository;

    public PrivateChatRoomCommandService(
            PrivateChatRoomRepository privateChatRoomRepository,
            MemberRepository memberRepository
    ) {
        this.privateChatRoomRepository = privateChatRoomRepository;
        this.memberRepository = memberRepository;
    }

    public InvitePrivateChatRoomResponse save(Long senderMemberId, Long receiverMemberId) {
        Member member = memberRepository.getById(senderMemberId);
        Member otherMember = memberRepository.getById(receiverMemberId);

        PrivateChatRoom chatRoom = privateChatRoomRepository
                .findByTwoMemberIds(member.getId(), otherMember.getId())
                .orElseGet(() -> privateChatRoomRepository.save(new PrivateChatRoom(member, otherMember)));

        return new InvitePrivateChatRoomResponse(chatRoom.getId());
    }

    public void leave(Long memberId, Long privateChatRoomId) {
        Member member = memberRepository.getById(memberId);
        PrivateChatRoom chatRoom = privateChatRoomRepository.getById(privateChatRoomId);

        chatRoom.leave(member);

        if (chatRoom.isEmpty()) {
            privateChatRoomRepository.delete(chatRoom);
        }
    }
}
