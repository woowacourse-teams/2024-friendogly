package com.happy.friendogly.chatroom.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.chatroom.dto.request.SaveChatRoomRequest;
import com.happy.friendogly.chatroom.dto.response.FindChatRoomMembersInfoResponse;
import com.happy.friendogly.chatroom.dto.response.FindClubDetailsResponse;
import com.happy.friendogly.chatroom.dto.response.FindMyChatRoomResponse;
import com.happy.friendogly.chatroom.dto.response.FindMyChatRoomResponseV2;
import com.happy.friendogly.chatroom.dto.response.SaveChatRoomResponse;
import com.happy.friendogly.chatroom.service.ChatRoomCommandService;
import com.happy.friendogly.chatroom.service.ChatRoomQueryService;
import com.happy.friendogly.common.ApiResponse;
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

    @GetMapping("/mine/v2")
    public ApiResponse<FindMyChatRoomResponseV2> findMineV2(@Auth Long memberId) {
        return ApiResponse.ofSuccess(chatRoomQueryService.findMineV2(memberId));
    }

    @GetMapping("/{chatRoomId}")
    public ApiResponse<List<FindChatRoomMembersInfoResponse>> findMemberInfo(
            @Auth Long memberId,
            @PathVariable Long chatRoomId
    ) {
        return ApiResponse.ofSuccess(chatRoomQueryService.findMemberInfo(memberId, chatRoomId));
    }

    @GetMapping("/{chatRoomId}/club")
    public ApiResponse<FindClubDetailsResponse> findClubDetails(@Auth Long memberId, @PathVariable Long chatRoomId) {
        FindClubDetailsResponse response = chatRoomQueryService.findClubDetails(memberId, chatRoomId);
        return ApiResponse.ofSuccess(response);
    }

    @PostMapping("/leave/{chatRoomId}")
    public ApiResponse<Void> leave(@Auth Long memberId, @PathVariable Long chatRoomId) {
        chatRoomCommandService.leave(memberId, chatRoomId);
        return ApiResponse.ofSuccess(null);
    }
}
