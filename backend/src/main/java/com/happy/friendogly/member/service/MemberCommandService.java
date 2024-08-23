package com.happy.friendogly.member.service;

import com.happy.friendogly.auth.domain.KakaoMember;
import com.happy.friendogly.auth.dto.TokenResponse;
import com.happy.friendogly.auth.repository.KakaoMemberRepository;
import com.happy.friendogly.auth.service.KakaoOauthClient;
import com.happy.friendogly.auth.service.jwt.JwtProvider;
import com.happy.friendogly.auth.service.jwt.TokenPayload;
import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.club.service.ClubCommandService;
import com.happy.friendogly.footprint.repository.FootprintRepository;
import com.happy.friendogly.infra.FileStorageManager;
import com.happy.friendogly.infra.ImageUpdateType;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.dto.request.SaveMemberRequest;
import com.happy.friendogly.member.dto.request.UpdateMemberRequest;
import com.happy.friendogly.member.dto.response.SaveMemberResponse;
import com.happy.friendogly.member.dto.response.UpdateMemberResponse;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.notification.repository.DeviceTokenRepository;
import com.happy.friendogly.pet.repository.PetRepository;
import com.happy.friendogly.utils.UuidGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class MemberCommandService {

    // TODO: 레전드
    private final JwtProvider jwtProvider;
    private final KakaoOauthClient kakaoOauthClient;
    private final FileStorageManager fileStorageManager;
    private final MemberRepository memberRepository;
    private final KakaoMemberRepository kakaoMemberRepository;
    private final ClubCommandService clubCommandService;
    private final ClubRepository clubRepository;
    private final PetRepository petRepository;
    private final FootprintRepository footprintRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    public MemberCommandService(
            JwtProvider jwtProvider, 
            KakaoOauthClient kakaoOauthClient,
            FileStorageManager fileStorageManager, 
            MemberRepository memberRepository,
            KakaoMemberRepository kakaoMemberRepository, 
            ClubCommandService clubCommandService,
            ClubRepository clubRepository, 
            PetRepository petRepository,
            FootprintRepository footprintRepository, 
            DeviceTokenRepository deviceTokenRepository
    ) {
        this.jwtProvider = jwtProvider;
        this.kakaoOauthClient = kakaoOauthClient;
        this.fileStorageManager = fileStorageManager;
        this.memberRepository = memberRepository;
        this.kakaoMemberRepository = kakaoMemberRepository;
        this.clubCommandService = clubCommandService;
        this.clubRepository = clubRepository;
        this.petRepository = petRepository;
        this.footprintRepository = footprintRepository;
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

    public UpdateMemberResponse update(Long memberId, UpdateMemberRequest request, MultipartFile image) {
        Member member = memberRepository.getById(memberId);
        ImageUpdateType imageUpdateType = ImageUpdateType.from(request.imageUpdateType());

        String newImageUrl = fileStorageManager.updateFile(member.getImageUrl(), image, imageUpdateType);
        member.update(request.name(), newImageUrl);

        return new UpdateMemberResponse(member);
    }

    public void delete(Long memberId) {
        // 해당 회원이 참여한 모임 및 채팅방에서 탈퇴
        clubRepository.findAllByParticipatingMemberId(memberId)
                .stream()
                .forEach(club -> clubCommandService.deleteClubMember(club.getId(), memberId));

        // 해당 회원의 발자국 삭제
        footprintRepository.deleteAllByMemberId(memberId);

        // 해당 회원의 반려견 삭제
        // TODO: 해당 반려견 이미지 S3에서 삭제
        petRepository.deleteAllByMemberId(memberId);

        // 카카오 회원가입 정보 삭제
        kakaoMemberRepository.deleteByMemberId(memberId);

        // 해당 회원의 device token 정보 삭제
        deviceTokenRepository.deleteByMemberId(memberId);

        // 회원 삭제
        // TODO: 해당 회원 이미지 S3에서 삭제
        memberRepository.deleteById(memberId);
    }
}
