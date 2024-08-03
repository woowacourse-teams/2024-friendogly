package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.chat.dto.request.InviteToChatRoomRequest;
import com.woowacourse.friendogly.chat.dto.response.SaveChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.ChatRoomRepository;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatRoomCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public ChatRoomCommandService(
            ChatRoomRepository chatRoomRepository,
            MemberRepository memberRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
    }

    public SaveChatRoomResponse save(Long memberId) {
        Member member = memberRepository.getById(memberId);
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.withInitialMemberOf(member));
        return new SaveChatRoomResponse(chatRoom.getId());
    }

    public void invite(Long senderMemberId, InviteToChatRoomRequest request) {
        Member sender = memberRepository.getById(senderMemberId);
        Member receiver = memberRepository.getById(request.receiverMemberId());
        ChatRoom chatRoom = chatRoomRepository.getById(request.chatRoomId());
        validateParticipation(chatRoom, sender);
        chatRoom.addMember(receiver);
    }

    public void enter(Long memberId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        Member member = memberRepository.getById(memberId);
        chatRoom.addMember(member);
    }

    public void leave(Long memberId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        Member member = memberRepository.getById(memberId);
        validateParticipation(chatRoom, member);
        chatRoom.removeMember(member);
    }

    private void validateParticipation(ChatRoom chatRoom, Member member) {
        // TODO: 웹 소켓에서는 예외 처리 로직을 다르게 가져가야 함. 400 에러가 터지더라도 정상적으로 채팅방에 들어가진다.
        if (!chatRoom.containsMember(member)) {
            throw new FriendoglyException("채팅에 참여한 상태가 아닙니다.");
        }
    }
}
