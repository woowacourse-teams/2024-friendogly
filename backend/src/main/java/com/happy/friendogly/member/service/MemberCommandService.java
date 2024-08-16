package com.happy.friendogly.member.service;

import com.happy.friendogly.auth.domain.KakaoMember;
import com.happy.friendogly.auth.dto.TokenResponse;
import com.happy.friendogly.auth.repository.KakaoMemberRepository;
import com.happy.friendogly.auth.service.KakaoOauthClient;
import com.happy.friendogly.auth.service.jwt.JwtProvider;
import com.happy.friendogly.auth.service.jwt.TokenPayload;
import com.happy.friendogly.infra.FileStorageManager;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.dto.request.SaveMemberRequest;
import com.happy.friendogly.member.dto.response.SaveMemberResponse;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.notification.repository.DeviceTokenRepository;
import com.happy.friendogly.utils.UuidGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class MemberCommandService {

    private final JwtProvider jwtProvider;
    private final KakaoOauthClient kakaoOauthClient;
    private final FileStorageManager fileStorageManager;
    private final MemberRepository memberRepository;
    private final KakaoMemberRepository kakaoMemberRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    public MemberCommandService(
            JwtProvider jwtProvider,
            KakaoOauthClient kakaoOauthClient,
            FileStorageManager fileStorageManager,
            MemberRepository memberRepository,
            KakaoMemberRepository kakaoMemberRepository,
            DeviceTokenRepository deviceTokenRepository
    ) {
        this.jwtProvider = jwtProvider;
        this.kakaoOauthClient = kakaoOauthClient;
        this.fileStorageManager = fileStorageManager;
        this.memberRepository = memberRepository;
        this.kakaoMemberRepository = kakaoMemberRepository;
        this.deviceTokenRepository = deviceTokenRepository;
    }

    public SaveMemberResponse saveMember(SaveMemberRequest request, MultipartFile image) {
        String imageUrl = "";
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageManager.uploadFile(image);
        }

        String kakaoMemberId = kakaoOauthClient.getUserInfo(request.accessToken()).id();

        Member member = Member.builder()
                .name(request.name())
                .tag(UuidGenerator.generateUuid())
                .imageUrl(imageUrl)
                .build();
        Member savedMember = memberRepository.save(member);

        TokenResponse tokens = jwtProvider.generateTokens(new TokenPayload(savedMember.getId()));
        kakaoMemberRepository.save(new KakaoMember(kakaoMemberId, savedMember.getId(), tokens.refreshToken()));

        return new SaveMemberResponse(savedMember, tokens);
    }
}
