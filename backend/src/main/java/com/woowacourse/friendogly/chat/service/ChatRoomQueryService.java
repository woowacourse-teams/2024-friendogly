package com.woowacourse.friendogly.chat.service;

import com.woowacourse.friendogly.chat.dto.response.FindMyChatRoomResponse;
import com.woowacourse.friendogly.chat.repository.ChatRoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;

    public List<FindMyChatRoomResponse> findMine(Long memberId) {
        return chatRoomRepository.findMine(memberId).stream()
                .map(FindMyChatRoomResponse::new)
                .toList();
    }
}
