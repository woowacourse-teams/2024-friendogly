package com.woowacourse.friendogly.member.controller;

import com.woowacourse.friendogly.member.dto.request.SaveMemberRequest;
import com.woowacourse.friendogly.member.dto.response.saveMemberResponse;
import com.woowacourse.friendogly.member.service.MemberCommandService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberCommandService memberCommandService;

    public MemberController(MemberCommandService memberCommandService) {
        this.memberCommandService = memberCommandService;
    }

    @PostMapping
    public ResponseEntity<Void> saveMember(@RequestBody SaveMemberRequest request){
        saveMemberResponse saveMemberResponse = memberCommandService.saveMember(request);
        return ResponseEntity.created(URI.create("/member/" + saveMemberResponse.id())).build();
    }
}
