package com.happy.friendogly.auth.service;

import com.happy.friendogly.auth.domain.KakaoMember;
import com.happy.friendogly.auth.dto.TokenResponse;
import com.happy.friendogly.auth.repository.KakaoMemberRepository;
import com.happy.friendogly.auth.service.jwt.JwtProvider;
import com.happy.friendogly.auth.service.jwt.TokenPayload;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {

    private final JwtProvider jwtProvider;
    private final KakaoMemberRepository kakaoMemberRepository;

    public AuthService(JwtProvider jwtProvider, KakaoMemberRepository kakaoMemberRepository) {
        this.jwtProvider = jwtProvider;
        this.kakaoMemberRepository = kakaoMemberRepository;
    }

    public TokenResponse refreshToken(String refreshToken) {
        jwtProvider.validateAndExtract(refreshToken);
        KakaoMember kakaoMember = kakaoMemberRepository.getByRefreshToken(refreshToken);

        TokenResponse tokens = jwtProvider.generateTokens(new TokenPayload(kakaoMember.getMemberId()));
        kakaoMember.updateRefreshToken(tokens.refreshToken());

        return tokens;
    }
}
