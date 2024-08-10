package com.woowacourse.friendogly.chat.service;

import static com.woowacourse.friendogly.chat.domain.MessageType.CHAT;

import com.woowacourse.friendogly.chat.domain.MessageType;
import com.woowacourse.friendogly.chat.dto.request.ChatMessageRequest;
import com.woowacourse.friendogly.chat.dto.response.ChatMessageResponse;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
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
