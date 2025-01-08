package com.happy.friendogly.chatmessage.service;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.happy.friendogly.chatmessage.domain.ChatMessage;
import com.happy.friendogly.chatroom.domain.ChatRoom;
import com.happy.friendogly.chatmessage.dto.request.FindMessagesByTimeRangeRequest;
import com.happy.friendogly.chatmessage.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.chatmessage.repository.ChatMessageRepository;
import com.happy.friendogly.chatroom.repository.ChatRoomRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChatMessageQueryService {

    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageQueryService(
            ChatMessageRepository chatMessageRepository,
            MemberRepository memberRepository,
            ChatRoomRepository chatRoomRepository
    ) {
        this.chatMessageRepository = chatMessageRepository;
        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<FindChatMessagesResponse> findAllByChatRoomId(Long memberId, Long chatRoomId) {
        validateParticipation(memberId, chatRoomId);
        List<ChatMessage> messages = chatMessageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
        return messages.stream()
                .map(FindChatMessagesResponse::new)
                .toList();
    }

    public List<FindChatMessagesResponse> findByTimeRange(
            Long memberId,
            Long chatRoomId,
            FindMessagesByTimeRangeRequest request
    ) {
        validateTimeRange(request.since(), request.until());
        validateParticipation(memberId, chatRoomId);

        List<ChatMessage> messages = chatMessageRepository
                .findAllByTimeRange(chatRoomId, request.since(), request.until());

        return messages.stream()
                .map(FindChatMessagesResponse::new)
                .toList();
    }

    private void validateTimeRange(LocalDateTime since, LocalDateTime until) {
        if (since.isAfter(until)) {
            throw new FriendoglyException("since 시간을 until 시간보다 과거로 설정해 주세요.");
        }
    }

    private void validateParticipation(Long memberId, Long chatRoomId) {
        Member member = memberRepository.getById(memberId);
        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);

        if (!chatRoom.containsMember(member)) {
            throw new FriendoglyException("채팅 내역을 조회할 수 있는 권한이 없습니다.", FORBIDDEN);
        }
    }
}
