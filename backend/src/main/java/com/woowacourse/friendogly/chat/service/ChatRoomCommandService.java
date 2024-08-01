package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.chat.repository.ChatRoomRepository;
import com.woowacourse.friendogly.club.repository.ClubRepository;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatRoomCommandService {

    // TODO: 미사용 클래스

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ClubRepository clubRepository;

    public ChatRoomCommandService(
            MemberRepository memberRepository,
            ChatRoomRepository chatRoomRepository,
            ClubRepository clubRepository
    ) {
        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.clubRepository = clubRepository;
    }
}
