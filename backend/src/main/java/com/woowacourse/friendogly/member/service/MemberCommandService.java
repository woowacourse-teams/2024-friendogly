package com.woowacourse.friendogly.member.service;

import com.woowacourse.friendogly.auth.domain.KakaoMember;
import com.woowacourse.friendogly.auth.dto.TokenResponse;
import com.woowacourse.friendogly.auth.repository.KakaoMemberRepository;
import com.woowacourse.friendogly.auth.service.KakaoOauthClient;
import com.woowacourse.friendogly.auth.service.jwt.JwtProvider;
import com.woowacourse.friendogly.auth.service.jwt.TokenPayload;
import com.woowacourse.friendogly.infra.FileStorageManager;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.dto.request.SaveMemberRequest;
import com.woowacourse.friendogly.member.dto.response.SaveMemberResponse;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.notification.repository.DeviceTokenRepository;
import com.woowacourse.friendogly.utils.UuidGenerator;
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
            DeviceTokenRepository deviceTokenRepository) {
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
        System.out.println("===============" + kakaoMemberId + "============");

        Member member = Member.builder()
                .name(request.name())
                .tag(UuidGenerator.generateUuid())
                .email(request.email())
                .imageUrl(imageUrl)
                .build();
        Member savedMember = memberRepository.save(member);

        TokenResponse tokens = jwtProvider.generateTokens(new TokenPayload(savedMember.getId()));
        kakaoMemberRepository.save(new KakaoMember(kakaoMemberId, savedMember.getId(), tokens.refreshToken()));
        return new SaveMemberResponse(savedMember, tokens);
    }
}
