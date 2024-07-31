package com.woowacourse.friendogly.chat.service;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.woowacourse.friendogly.chat.domain.PrivateChatRoom;
import com.woowacourse.friendogly.chat.dto.response.InvitePrivateChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.PrivateChatRoomRepository;
import com.woowacourse.friendogly.exception.FriendoglyException;
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
        Member member = memberRepository.findById(senderMemberId)
                .orElseThrow(() -> new FriendoglyException(senderMemberId + "는 존재하지 않는 Member ID입니다."));
        Member otherMember = memberRepository.findById(receiverMemberId)
                .orElseThrow(() -> new FriendoglyException(receiverMemberId + "는 존재하지 않는 Member ID입니다."));

        if (exists(member, otherMember)) {
            PrivateChatRoom oldChatRoom = privateChatRoomRepository.findByMemberAndOtherMember(member, otherMember)
                    .or(() -> privateChatRoomRepository.findByMemberAndOtherMember(otherMember, member))
                    .orElseThrow(() -> new FriendoglyException("알 수 없는 에러 발생", INTERNAL_SERVER_ERROR));
            return new InvitePrivateChatRoomResponse(oldChatRoom.getId());
        }

        PrivateChatRoom newChatRoom = privateChatRoomRepository.save(new PrivateChatRoom(member, otherMember));
        return new InvitePrivateChatRoomResponse(newChatRoom.getId());
    }

    private boolean exists(Member member, Member otherMember) {
        return privateChatRoomRepository.existsByMemberAndOtherMember(member, otherMember)
               || privateChatRoomRepository.existsByMemberAndOtherMember(otherMember, member);
    }

    public void leave(Long memberId, Long privateChatRoomId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FriendoglyException(memberId + "는 존재하지 않는 Member ID입니다."));
        PrivateChatRoom chatRoom = privateChatRoomRepository.findById(privateChatRoomId)
                .orElseThrow(() -> new FriendoglyException(memberId + "는 존재하지 않는 PrivateChatRoom ID입니다."));

        chatRoom.leave(member);

        if (chatRoom.isEmpty()) {
            privateChatRoomRepository.delete(chatRoom);
        }
    }
}
