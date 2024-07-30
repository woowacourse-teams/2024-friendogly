package com.woowacourse.friendogly.chat.controller;

import com.woowacourse.friendogly.chat.service.PrivateChatRoomCommandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-rooms/private")
public class PrivateChatRoomController {

    private final PrivateChatRoomCommandService privateChatRoomCommandService;

    public PrivateChatRoomController(PrivateChatRoomCommandService privateChatRoomCommandService) {
        this.privateChatRoomCommandService = privateChatRoomCommandService;
    }
}
