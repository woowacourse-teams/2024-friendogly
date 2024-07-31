package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.chat.dto.request.ChatMessageRequest;
import com.woowacourse.friendogly.chat.dto.response.ChatMessageResponse;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final MemberRepository memberRepository;

    public ChatService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public ChatMessageResponse parseMessage(Long memberId, ChatMessageRequest request) {
        Member member = memberRepository.getById(memberId);
        return new ChatMessageResponse(member.getName().getValue(), request.content());
    }
}
