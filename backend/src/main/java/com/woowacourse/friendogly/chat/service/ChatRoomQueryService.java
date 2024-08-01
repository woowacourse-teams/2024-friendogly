package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.repository.ClubRepository;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChatRoomQueryService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;

    public ChatRoomQueryService(MemberRepository memberRepository, ClubRepository clubRepository) {
        this.memberRepository = memberRepository;
        this.clubRepository = clubRepository;
    }

    public void validate(Long memberId, Long chatRoomId) {
        Club club = clubRepository.getByChatRoomId(chatRoomId);
        Member member = memberRepository.getById(memberId);
        if (!club.isAlreadyJoined(member)) {
            throw new FriendoglyException("모임의 구성원이 아닙니다.");
        }
    }
}
