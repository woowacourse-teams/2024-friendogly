package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.chat.dto.ChatMessageRequest;
import com.woowacourse.friendogly.chat.dto.ChatMessageResponse;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final MemberRepository memberRepository;

    public ChatService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public ChatMessageResponse parseData(Long memberId, ChatMessageRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 Member ID입니다."));

        return new ChatMessageResponse(member.getName().getValue(), request.content());
    }
}
