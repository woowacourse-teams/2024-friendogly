package com.happy.friendogly.playground.service;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.playground.domain.Location;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import com.happy.friendogly.playground.dto.request.SavePlaygroundRequest;
import com.happy.friendogly.playground.dto.response.SavePlaygroundResponse;
import com.happy.friendogly.playground.repository.PlaygroundMemberRepository;
import com.happy.friendogly.playground.repository.PlaygroundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaygroundCommandService {

    private final PlaygroundRepository playgroundRepository;
    private final PlaygroundMemberRepository playgroundMemberRepository;
    private final MemberRepository memberRepository;

    public PlaygroundCommandService(
            PlaygroundRepository playgroundRepository,
            PlaygroundMemberRepository playgroundMemberRepository,
            MemberRepository memberRepository) {
        this.playgroundRepository = playgroundRepository;
        this.playgroundMemberRepository = playgroundMemberRepository;
        this.memberRepository = memberRepository;
    }

    public SavePlaygroundResponse save(SavePlaygroundRequest request, Long memberId) {
        Member member = memberRepository.getById(memberId);
        validateExistParticipatingPlayground(member);
        Playground savedPlayground = playgroundRepository.save(
                new Playground(new Location(request.latitude(), request.longitude()))
        );
        playgroundMemberRepository.save(new PlaygroundMember(savedPlayground, member));
        return SavePlaygroundResponse.from(savedPlayground);
    }

    private void validateExistParticipatingPlayground(Member member) {
        if (playgroundMemberRepository.existsByMemberId(member.getId())) {
            throw new FriendoglyException("이미 참여한 놀이터가 존재합니다.");
        }
    }
}
