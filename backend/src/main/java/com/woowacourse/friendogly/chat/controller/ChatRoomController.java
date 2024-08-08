package com.woowacourse.friendogly.chat.controller;

import com.woowacourse.friendogly.auth.Auth;
import com.woowacourse.friendogly.chat.dto.request.SaveChatRoomRequest;
import com.woowacourse.friendogly.chat.dto.response.FindChatRoomMembersInfoResponse;
import com.woowacourse.friendogly.chat.dto.response.FindMyChatRoomResponse;
import com.woowacourse.friendogly.chat.dto.response.SaveChatRoomResponse;
import com.woowacourse.friendogly.chat.service.ChatRoomCommandService;
import com.woowacourse.friendogly.chat.service.ChatRoomQueryService;
import com.woowacourse.friendogly.common.ApiResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-rooms")
public class ChatRoomController {

    private final ChatRoomQueryService chatRoomQueryService;
    private final ChatRoomCommandService chatRoomCommandService;

    public ChatRoomController(
            ChatRoomQueryService chatRoomQueryService,
            ChatRoomCommandService chatRoomCommandService
    ) {
        this.chatRoomQueryService = chatRoomQueryService;
        this.chatRoomCommandService = chatRoomCommandService;
    }

    @PostMapping
    public ApiResponse<SaveChatRoomResponse> savePrivate(@Auth Long memberId, SaveChatRoomRequest request) {
        return ApiResponse.ofSuccess(chatRoomCommandService.savePrivate(memberId, request));
    }

    @GetMapping("/mine")
    public ApiResponse<FindMyChatRoomResponse> findMine(@Auth Long memberId) {
        return ApiResponse.ofSuccess(chatRoomQueryService.findMine(memberId));
    }

    @GetMapping("/{chatRoomId}")
    public List<FindChatRoomMembersInfoResponse> findMemberInfo(@Auth Long memberId, @PathVariable Long chatRoomId) {
        return chatRoomQueryService.findMemberInfo(memberId, chatRoomId);
    }
}
