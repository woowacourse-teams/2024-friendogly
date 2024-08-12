package com.happy.friendogly.chat.service;

import static com.happy.friendogly.chat.domain.MessageType.CHAT;

import com.happy.friendogly.chat.domain.MessageType;
import com.happy.friendogly.chat.dto.request.ChatMessageRequest;
import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatService {

    private final MemberRepository memberRepository;

    public ChatService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public ChatMessageResponse parseNotice(MessageType messageType, Long senderMemberId, LocalDateTime createdAt) {
        Member senderMember = memberRepository.getById(senderMemberId);
        return new ChatMessageResponse(messageType, "", senderMember, createdAt);
    }

    public ChatMessageResponse parseMessage(Long senderMemberId, ChatMessageRequest request, LocalDateTime createdAt) {
        Member senderMember = memberRepository.getById(senderMemberId);
        return new ChatMessageResponse(CHAT, request.content(), senderMember, createdAt);
    }
}
