package com.happy.friendogly.chat.service;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.happy.friendogly.chat.domain.ChatMessage;
import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.chat.repository.ChatMessageRepository;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChatQueryService {

    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatQueryService(
            ChatMessageRepository chatMessageRepository,
            MemberRepository memberRepository,
            ChatRoomRepository chatRoomRepository
    ) {
        this.chatMessageRepository = chatMessageRepository;
        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<FindChatMessagesResponse> findAllByChatRoomId(Long memberId, Long chatRoomId) {
        validateParticipation(memberId, chatRoomId);
        List<ChatMessage> messages = chatMessageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
        return messages.stream()
                .map(FindChatMessagesResponse::new)
                .toList();
    }

    public List<FindChatMessagesResponse> findRecent(Long memberId, Long chatRoomId, LocalDateTime since) {
        validateParticipation(memberId, chatRoomId);
        List<ChatMessage> messages = chatMessageRepository.findRecent(chatRoomId, since);
        return messages.stream()
                .map(FindChatMessagesResponse::new)
                .toList();
    }

    private void validateParticipation(Long memberId, Long chatRoomId) {
        Member member = memberRepository.getById(memberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);

        if (!chatRoom.containsMember(member)) {
            throw new FriendoglyException("채팅 내역을 조회할 수 있는 권한이 없습니다.", FORBIDDEN);
        }
    }
}
