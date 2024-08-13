package com.happy.friendogly.member.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.common.ApiResponse;
import com.happy.friendogly.member.dto.request.SaveMemberRequest;
import com.happy.friendogly.member.dto.request.UpdateMemberRequest;
import com.happy.friendogly.member.dto.response.FindMemberResponse;
import com.happy.friendogly.member.dto.response.SaveMemberResponse;
import com.happy.friendogly.member.dto.response.UpdateMemberResponse;
import com.happy.friendogly.member.service.MemberCommandService;
import com.happy.friendogly.member.service.MemberQueryService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    public MemberController(MemberQueryService memberQueryService, MemberCommandService memberCommandService) {
        this.memberQueryService = memberQueryService;
        this.memberCommandService = memberCommandService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SaveMemberResponse>> saveMember(
            @RequestPart @Valid SaveMemberRequest request,
            @RequestPart(required = false) MultipartFile image
    ) {
        SaveMemberResponse response = memberCommandService.saveMember(request, image);
        return ResponseEntity.created(URI.create("/members/" + response.id()))
                .body(ApiResponse.ofSuccess(response));
    }

    @GetMapping("/mine")
    public ApiResponse<FindMemberResponse> findMine(@Auth Long memberId) {
        FindMemberResponse response = memberQueryService.findById(memberId);
        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<FindMemberResponse> findById(@PathVariable Long id) {
        FindMemberResponse response = memberQueryService.findById(id);
        return ApiResponse.ofSuccess(response);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UpdateMemberResponse>> update(
            @Auth Long memberId,
            @RequestPart @Valid UpdateMemberRequest request,
            @RequestPart(required = false) MultipartFile image
    ) {
        UpdateMemberResponse response = memberCommandService.update(memberId, request, image);
        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }
}
