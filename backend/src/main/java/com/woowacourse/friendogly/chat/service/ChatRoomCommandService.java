package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.chat.domain.ChatRoom;
import com.woowacourse.friendogly.chat.repository.ChatRoomRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatRoomCommandService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomCommandService(
            MemberRepository memberRepository,
            ChatRoomRepository chatRoomRepository
    ) {
        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public void leave(Long memberId, Long chatRoomId) {
        Member member = memberRepository.getById(memberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
        chatRoom.removeMember(member);
    }
}
