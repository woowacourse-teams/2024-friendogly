package com.woowacourse.friendogly.chat.service;

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

    public ChatMessageResponse parseMessage(Long memberId, ChatMessageRequest request) {
        Member member = memberRepository.getById(memberId);
        return new ChatMessageResponse(member.getName().getValue(), request.content());
    }

    public ChatMessageResponse parseEnterMessage(Long memberId) {
        Member member = memberRepository.getById(memberId);
        return new ChatMessageResponse(member.getName().getValue(), "님이 방을 들어왔습니다.");
    }

    public ChatMessageResponse parseLeaveMessage(Long memberId) {
        Member member = memberRepository.getById(memberId);
        return new ChatMessageResponse(member.getName().getValue(), "님이 방을 나갔습니다.");
    }
}
