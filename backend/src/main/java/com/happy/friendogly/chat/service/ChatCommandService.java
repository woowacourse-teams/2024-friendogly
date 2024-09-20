package com.happy.friendogly.chat.service;

import static com.happy.friendogly.chat.domain.MessageType.CHAT;
import static com.happy.friendogly.chat.domain.MessageType.ENTER;
import static com.happy.friendogly.chat.domain.MessageType.LEAVE;

import com.happy.friendogly.chat.domain.ChatMessage;
import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.domain.MessageType;
import com.happy.friendogly.chat.dto.request.ChatMessageRequest;
import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
import com.happy.friendogly.chat.repository.ChatMessageRepository;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.notification.service.NotificationService;
import java.time.LocalDateTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatCommandService {

    private static final String TOPIC_CHAT_PREFIX = "/topic/chat/";
    private static final String EMPTY_CONTENT = "";

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate template;

    public ChatCommandService(
            MemberRepository memberRepository,
            ChatRoomRepository chatRoomRepository,
            ChatMessageRepository chatMessageRepository,
            NotificationService notificationService,
            SimpMessagingTemplate template
    ) {
        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.notificationService = notificationService;
        this.template = template;
    }

    public void enter(Long senderMemberId, Long chatRoomId) {
        Member senderMember = memberRepository.getById(senderMemberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);

        if (chatRoom.isGroupChat()) {
            chatRoom.addMember(senderMember);
        }

        sendAndSave(ENTER, EMPTY_CONTENT, chatRoom, senderMember);
    }

    public void sendChat(Long senderMemberId, Long chatRoomId, ChatMessageRequest request) {
        Member senderMember = memberRepository.getById(senderMemberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);

        if (!chatRoom.containsMember(senderMember)) {
            throw new FriendoglyException("자신이 참여한 채팅방에만 메시지를 보낼 수 있습니다.");
        }

        sendAndSave(CHAT, request.content(), chatRoom, senderMember);
    }

    public void leave(Long senderMemberId, Long chatRoomId) {
        Member senderMember = memberRepository.getById(senderMemberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        chatRoom.removeMember(senderMember);
        sendAndSave(LEAVE, EMPTY_CONTENT, chatRoom, senderMember);
    }

    private void sendAndSave(MessageType messageType, String content, ChatRoom chatRoom, Member senderMember) {
        ChatMessageResponse chat = new ChatMessageResponse(messageType, content, senderMember, LocalDateTime.now());
        notificationService.sendChatNotification(chatRoom.getId(), chat);
        template.convertAndSend(TOPIC_CHAT_PREFIX + chatRoom.getId(), chat);
        chatMessageRepository.save(new ChatMessage(chatRoom, messageType, senderMember, content));
    }
}
