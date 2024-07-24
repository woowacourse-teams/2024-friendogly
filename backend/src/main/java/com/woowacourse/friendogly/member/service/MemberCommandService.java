package com.woowacourse.friendogly.member.service;

import com.woowacourse.friendogly.infra.FileStorageManager;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.dto.request.SaveMemberRequest;
import com.woowacourse.friendogly.member.dto.response.SaveMemberResponse;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.utils.UuidGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class MemberCommandService {

    private final FileStorageManager fileStorageManager;
    private final MemberRepository memberRepository;

    public MemberCommandService(FileStorageManager fileStorageManager, MemberRepository memberRepository) {
        this.fileStorageManager = fileStorageManager;
        this.memberRepository = memberRepository;
    }

    public SaveMemberResponse saveMember(SaveMemberRequest request, MultipartFile image) {
        String imageUrl = "";
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageManager.uploadFile(image);
        }

        Member member = Member.builder()
                .name(request.name())
                .tag(UuidGenerator.generateUuid())
                .email(request.email())
                .imageUrl(imageUrl)
                .build();
        Member savedMember = memberRepository.save(member);

        return new SaveMemberResponse(savedMember);
    }
}
