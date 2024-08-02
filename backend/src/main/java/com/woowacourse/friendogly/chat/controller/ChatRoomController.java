package com.woowacourse.friendogly.chat.controller;

import com.woowacourse.friendogly.auth.Auth;
import com.woowacourse.friendogly.chat.dto.response.FindChatRoomMembersInfoResponse;
import com.woowacourse.friendogly.chat.dto.response.FindMyChatRoomResponse;
import com.woowacourse.friendogly.chat.service.ChatRoomQueryService;
import com.woowacourse.friendogly.common.ApiResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-rooms")
public class ChatRoomController {

    private final ChatRoomQueryService chatRoomQueryService;

    public ChatRoomController(ChatRoomQueryService chatRoomQueryService) {
        this.chatRoomQueryService = chatRoomQueryService;
    }

    @GetMapping("/mine")
    public ApiResponse<List<FindMyChatRoomResponse>> findMine(@Auth Long memberId) {
        return ApiResponse.ofSuccess(chatRoomQueryService.findMine(memberId));
    }

    @GetMapping("/{chatRoomId}")
    public List<FindChatRoomMembersInfoResponse> findMemberInfo(@PathVariable Long chatRoomId) {
        return chatRoomQueryService.findMemberInfo(chatRoomId);
    }
}
