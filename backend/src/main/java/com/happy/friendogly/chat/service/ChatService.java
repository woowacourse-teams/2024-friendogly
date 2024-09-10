package com.happy.friendogly.chat.service;

import static com.happy.friendogly.chat.domain.MessageType.CHAT;
import static com.happy.friendogly.chat.domain.MessageType.ENTER;
import static com.happy.friendogly.chat.domain.MessageType.LEAVE;

import com.happy.friendogly.chat.domain.ChatMessage;
import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.dto.request.ChatMessageRequest;
import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
import com.happy.friendogly.chat.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.chat.repository.ChatMessageRepository;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
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

    public List<FindChatMessagesResponse> findAllByChatRoomId(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
        return messages.stream()
                .map(message -> new FindChatMessagesResponse(
                        message.getMessageType(),
                        message.getContent(),
                        message.getSenderMember(),
                        message.getCreatedAt()
                )).toList();
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
