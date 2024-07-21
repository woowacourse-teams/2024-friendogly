package com.woowacourse.friendogly.member.service;

import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.dto.request.SaveMemberRequest;
import com.woowacourse.friendogly.member.dto.response.SaveMemberResponse;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.utils.UuidGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberCommandService {

    private final MemberRepository memberRepository;

    public MemberCommandService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public SaveMemberResponse saveMember(SaveMemberRequest request) {
        Member member = Member.builder()
                .name(request.name())
                .tag(UuidGenerator.generateUuid())
                .email(request.email())
                .build();
        Member savedMember = memberRepository.save(member);

        return new SaveMemberResponse(savedMember);
    }
}
