package com.happy.friendogly.chat.service;

import static com.happy.friendogly.chat.domain.MessageType.CHAT;
import static com.happy.friendogly.chat.domain.MessageType.IMAGE;

import com.happy.friendogly.chat.domain.MessageType;
import com.happy.friendogly.chat.dto.request.ChatMessageRequest;
import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
import com.happy.friendogly.infra.FileStorageManager;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ChatService {

    private final MemberRepository memberRepository;
    private final FileStorageManager fileStorageManager;

    public ChatService(
            MemberRepository memberRepository,
            FileStorageManager fileStorageManager
    ) {
        this.memberRepository = memberRepository;
        this.fileStorageManager = fileStorageManager;
    }

    public ChatMessageResponse parseNotice(MessageType messageType, Long senderMemberId, LocalDateTime createdAt) {
        Member senderMember = memberRepository.getById(senderMemberId);
        return new ChatMessageResponse(messageType, "", senderMember, createdAt);
    }

    public ChatMessageResponse parseTextMessage(
            Long senderMemberId,
            ChatMessageRequest request,
            LocalDateTime createdAt
    ) {
        Member senderMember = memberRepository.getById(senderMemberId);
        return new ChatMessageResponse(CHAT, request.content(), senderMember, createdAt);
    }

    public ChatMessageResponse parseImageMessage(
            Long senderMemberId,
            MultipartFile image,
            LocalDateTime createdAt
    ) {
        Member senderMember = memberRepository.getById(senderMemberId);
        String imageUrl = fileStorageManager.uploadFile(image);
        return new ChatMessageResponse(IMAGE, imageUrl, senderMember, createdAt);
    }
}
