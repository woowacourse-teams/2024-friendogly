package com.happy.friendogly.chat.service;

import static com.happy.friendogly.chat.domain.MessageType.CHAT;
import static com.happy.friendogly.chat.domain.MessageType.ENTER;
import static com.happy.friendogly.chat.domain.MessageType.LEAVE;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.happy.friendogly.chat.domain.ChatMessage;
import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.ChatMessageRequest;
import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
import com.happy.friendogly.chat.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.chat.repository.ChatMessageRepository;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.notification.service.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate template;

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

    public void enter(Long senderMemberId, Long chatRoomId) {
        Member senderMember = memberRepository.getById(senderMemberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);

        if (chatRoom.isGroupChat()) {
            chatRoom.addMember(senderMember);
        }

        ChatMessageResponse chat = new ChatMessageResponse(ENTER, "", senderMember, LocalDateTime.now());
        notificationService.sendChatNotification(chatRoomId, chat);
        template.convertAndSend("/topic/chat/" + chatRoomId, chat);
        chatMessageRepository.save(new ChatMessage(chatRoom, ENTER, senderMember, ""));
    }

    public void sendChat(Long senderMemberId, Long chatRoomId, ChatMessageRequest request) {
        Member senderMember = memberRepository.getById(senderMemberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);

        if (!chatRoom.containsMember(senderMember)) {
            throw new FriendoglyException("자신이 참여한 채팅방에만 메시지를 보낼 수 있습니다.");
        }

        ChatMessageResponse chat = new ChatMessageResponse(CHAT, request.content(), senderMember, LocalDateTime.now());
        notificationService.sendChatNotification(chatRoomId, chat);
        template.convertAndSend("/topic/chat/" + chatRoomId, chat);
        chatMessageRepository.save(new ChatMessage(chatRoom, CHAT, senderMember, request.content()));
    }

    public void leave(Long senderMemberId, Long chatRoomId) {
        Member senderMember = memberRepository.getById(senderMemberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        chatRoom.removeMember(senderMember);

        ChatMessageResponse chat = new ChatMessageResponse(LEAVE, "", senderMember, LocalDateTime.now());
        notificationService.sendChatNotification(chatRoomId, chat);
        template.convertAndSend("/topic/chat/" + chatRoomId, chat);
        chatMessageRepository.save(new ChatMessage(chatRoom, LEAVE, senderMember, ""));
    }
}
