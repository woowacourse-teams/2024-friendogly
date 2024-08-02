package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.club.repository.ClubRepository;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatRoomCommandService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;

    public ChatRoomCommandService(
            MemberRepository memberRepository,
            ClubRepository clubRepository
    ) {
        this.memberRepository = memberRepository;
        this.clubRepository = clubRepository;
    }

    public void enter(Long memberId, Long chatRoomId) {
        Club club = clubRepository.getByChatRoomId(chatRoomId);
        Member member = memberRepository.getById(memberId);
        validateParticipation(club, member);
        club.addMemberToChat(member);
    }

    public void leave(Long memberId, Long chatRoomId) {
        Club club = clubRepository.getByChatRoomId(chatRoomId);
        Member member = memberRepository.getById(memberId);
        validateParticipation(club, member);
        club.removeMemberFromChat(member);
    }

    private void validateParticipation(Club club, Member member) {
        // TODO: 웹 소켓에서는 예외 처리 로직을 다르게 가져가야 함. 400 에러가 터지더라도 정상적으로 채팅방에 들어가진다.
        if (!club.isAlreadyJoined(member)) {
            throw new FriendoglyException("모임의 구성원이 아닙니다.");
        }
    }
}
