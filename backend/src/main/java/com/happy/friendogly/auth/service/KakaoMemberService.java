package com.happy.friendogly.auth.service;

import com.happy.friendogly.auth.domain.KakaoMember;
import com.happy.friendogly.auth.dto.KakaoLoginRequest;
import com.happy.friendogly.auth.dto.KakaoLoginResponse;
import com.happy.friendogly.auth.dto.TokenResponse;
import com.happy.friendogly.auth.repository.KakaoMemberRepository;
import com.happy.friendogly.auth.service.jwt.JwtProvider;
import com.happy.friendogly.auth.service.jwt.TokenPayload;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KakaoMemberService {

    private final JwtProvider jwtProvider;
    private final KakaoOauthClient kakaoOauthClient;
    private final KakaoMemberRepository kakaoMemberRepository;

    public KakaoMemberService(
            JwtProvider jwtProvider,
            KakaoOauthClient kakaoOauthClient,
            KakaoMemberRepository kakaoMemberRepository
    ) {
        this.jwtProvider = jwtProvider;
        this.kakaoOauthClient = kakaoOauthClient;
        this.kakaoMemberRepository = kakaoMemberRepository;
    }

    public KakaoLoginResponse login(KakaoLoginRequest request) {
        String kakaoMemberId = kakaoOauthClient.getUserInfo(request.accessToken()).id();

        Optional<KakaoMember> kakaoMemberOptional = kakaoMemberRepository.findByKakaoMemberId(kakaoMemberId);
        if (kakaoMemberOptional.isPresent()) {
            KakaoMember kakaoMember = kakaoMemberOptional.get();

            TokenResponse tokens = jwtProvider.generateTokens(new TokenPayload(kakaoMember.getMemberId()));
            kakaoMember.updateRefreshToken(tokens.refreshToken());

            return KakaoLoginResponse.ofRegistered(tokens);
        }
        return KakaoLoginResponse.ofNotRegistered();
    }

    public void logout(Long memberId) {
        KakaoMember kakaoMember = kakaoMemberRepository.getByMemberId(memberId);
//        kakaoOauthClient.logout(kakaoMember.getKakaoMemberId());
        kakaoMember.updateRefreshToken(null);
    }
}
