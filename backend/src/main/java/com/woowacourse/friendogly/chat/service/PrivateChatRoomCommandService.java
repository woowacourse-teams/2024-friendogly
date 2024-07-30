package com.woowacourse.friendogly.chat.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.woowacourse.friendogly.chat.domain.PrivateChatRoom;
import com.woowacourse.friendogly.chat.dto.response.InvitePrivateChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.PrivateChatRoomRepository;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
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

        if (member.getId().equals(otherMember.getId())) { // TODO: equals and hashCode 구현 후 변경
            throw new FriendoglyException("자기 자신을 채팅방에 초대할 수 없습니다.", BAD_REQUEST);
        }

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
}
