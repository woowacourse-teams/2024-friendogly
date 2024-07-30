package com.woowacourse.friendogly.chat.controller;

import com.woowacourse.friendogly.auth.Auth;
import com.woowacourse.friendogly.chat.dto.response.FindMyPrivateChatRoomResponse;
import com.woowacourse.friendogly.chat.service.PrivateChatRoomQueryService;
import com.woowacourse.friendogly.common.ApiResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-rooms/private")
public class PrivateChatRoomController {

    private final PrivateChatRoomQueryService privateChatRoomQueryService;

    public PrivateChatRoomController(PrivateChatRoomQueryService privateChatRoomQueryService) {
        this.privateChatRoomQueryService = privateChatRoomQueryService;
    }

    @GetMapping
    public ApiResponse<List<FindMyPrivateChatRoomResponse>> findMine(@Auth Long memberId) {
        return ApiResponse.ofSuccess(privateChatRoomQueryService.findMine(memberId));
    }
}
