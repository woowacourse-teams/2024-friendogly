package com.happy.friendogly.chatsocket.service;

import static com.happy.friendogly.chatsocket.domain.MessageType.CHAT;
import static com.happy.friendogly.chatsocket.domain.MessageType.ENTER;
import static com.happy.friendogly.chatsocket.domain.MessageType.LEAVE;

import com.happy.friendogly.chatmessage.domain.ChatMessage;
import com.happy.friendogly.chatmessage.repository.ChatMessageRepository;
import com.happy.friendogly.chatroom.domain.ChatRoom;
import com.happy.friendogly.chatroom.repository.ChatRoomRepository;
import com.happy.friendogly.chatsocket.domain.MessageType;
import com.happy.friendogly.chatsocket.dto.request.ChatMessageSocketRequest;
import com.happy.friendogly.chatsocket.dto.response.ChatMessageSocketResponse;
import com.happy.friendogly.chatsocket.template.ChatTemplate;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.notification.service.ChatNotificationService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatSocketCommandService {

    private static final String EMPTY_CONTENT = "";

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatNotificationService chatNotificationService;
    private final ChatTemplate template;

    public ChatSocketCommandService(
            MemberRepository memberRepository,
            ClubRepository clubRepository,
            ChatRoomRepository chatRoomRepository,
            ChatMessageRepository chatMessageRepository,
            ChatNotificationService chatNotificationService,
            ChatTemplate template
    ) {
        this.memberRepository = memberRepository;
        this.clubRepository = clubRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatNotificationService = chatNotificationService;
        this.template = template;
    }

    public void sendEnter(Long senderMemberId, Long chatRoomId) {
        Member senderMember = memberRepository.getById(senderMemberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        sendAndSave(ENTER, EMPTY_CONTENT, chatRoom, senderMember);
    }

    public void sendChat(Long senderMemberId, Long chatRoomId, ChatMessageSocketRequest request) {
        Member senderMember = memberRepository.getById(senderMemberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);

        if (!chatRoom.containsMember(senderMember)) {
            throw new FriendoglyException("자신이 참여한 채팅방에만 메시지를 보낼 수 있습니다.");
        }

        sendAndSave(CHAT, request.content(), chatRoom, senderMember);
    }

    public void sendLeave(Long senderMemberId, Long chatRoomId) {
        Member senderMember = memberRepository.getById(senderMemberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        sendAndSave(LEAVE, EMPTY_CONTENT, chatRoom, senderMember);
    }

    private void sendAndSave(MessageType messageType, String content, ChatRoom chatRoom, Member senderMember) {
        ChatMessageSocketResponse chat = new ChatMessageSocketResponse(
                messageType, content, senderMember, LocalDateTime.now());
        Club club = clubRepository.getByChatRoomId(chatRoom.getId());

        chatNotificationService.sendChatNotification(chatRoom.getId(), chat, club);
        template.convertAndSend(chatRoom.getId(), chat);
        chatMessageRepository.save(new ChatMessage(chatRoom, messageType, senderMember, content));
    }
}
