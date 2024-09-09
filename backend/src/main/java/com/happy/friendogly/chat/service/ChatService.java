package com.happy.friendogly.chat.service;

import static com.happy.friendogly.chat.domain.MessageType.CHAT;

import com.happy.friendogly.chat.domain.MessageType;
import com.happy.friendogly.chat.dto.request.ChatMessageRequest;
import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.notification.service.NotificationService;
import java.time.LocalDateTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatService {

    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate template;

    public ChatService(
            MemberRepository memberRepository,
            NotificationService notificationService,
            SimpMessagingTemplate template
    ) {
        this.memberRepository = memberRepository;
        this.notificationService = notificationService;
        this.template = template;
    }

    public ChatMessageResponse parseNotice(MessageType messageType, Long senderMemberId, LocalDateTime createdAt) {
        Member senderMember = memberRepository.getById(senderMemberId);
        return new ChatMessageResponse(messageType, "", senderMember, createdAt);
    }

    public void send(Long senderMemberId, Long chatRoomId, ChatMessageRequest request) {
        Member senderMember = memberRepository.getById(senderMemberId);
        ChatMessageResponse response
                = new ChatMessageResponse(CHAT, request.content(), senderMember, LocalDateTime.now());
        notificationService.sendChatNotification(chatRoomId, response);
        template.convertAndSend("/topic/chat/" + chatRoomId, response);
    }
}
