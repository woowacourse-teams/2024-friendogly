package com.woowacourse.friendogly.chat.service;

import static com.woowacourse.friendogly.chat.domain.MessageType.CHAT;

import com.woowacourse.friendogly.chat.domain.MessageType;
import com.woowacourse.friendogly.chat.dto.request.ChatMessageRequest;
import com.woowacourse.friendogly.chat.dto.response.ChatMessageResponse;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatService {

    private final MemberRepository memberRepository;

    public ChatService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public ChatMessageResponse parseNotice(MessageType messageType, Long memberId) {
        Member member = memberRepository.getById(memberId);
        return new ChatMessageResponse(messageType, member.getName().getValue(), "");
    }

    public ChatMessageResponse parseMessage(Long memberId, ChatMessageRequest request) {
        Member member = memberRepository.getById(memberId);
        return new ChatMessageResponse(CHAT, member.getName().getValue(), request.content());
    }
}
