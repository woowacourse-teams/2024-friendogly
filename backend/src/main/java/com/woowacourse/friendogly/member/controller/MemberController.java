package com.woowacourse.friendogly.member.controller;

import com.woowacourse.friendogly.common.ApiResponse;
import com.woowacourse.friendogly.member.dto.request.SaveMemberRequest;
import com.woowacourse.friendogly.member.dto.response.SaveMemberResponse;
import com.woowacourse.friendogly.member.service.MemberCommandService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberCommandService memberCommandService;

    public MemberController(MemberCommandService memberCommandService) {
        this.memberCommandService = memberCommandService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SaveMemberResponse>> saveMember(@RequestBody @Valid SaveMemberRequest request) {
        SaveMemberResponse response = memberCommandService.saveMember(request);
        return ResponseEntity.created(URI.create("/members/" + response.id()))
                .body(ApiResponse.ofSuccess(response));
    }
}
