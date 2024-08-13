package com.happy.friendogly.member.service;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.dto.response.FindMemberResponse;
import com.happy.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public FindMemberResponse findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FriendoglyException("존재하지 않는 회원입니다."));
        return new FindMemberResponse(member);
    }

    public Member getById(Long memberId) {
        return memberRepository.getById(memberId);
    }
}
