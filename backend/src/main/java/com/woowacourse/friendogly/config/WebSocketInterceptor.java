package com.woowacourse.friendogly.config;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.messaging.simp.stomp.StompCommand.SUBSCRIBE;

import com.woowacourse.friendogly.chat.repository.ChatRoomMemberRepository;
import com.woowacourse.friendogly.exception.FriendoglyWebSocketException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebSocketInterceptor implements ChannelInterceptor {

    private static final String TOPIC_CHAT_ENDPOINT = "/topic/chat/";

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public WebSocketInterceptor(ChatRoomMemberRepository chatRoomMemberRepository) {
        this.chatRoomMemberRepository = chatRoomMemberRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getCommand() == SUBSCRIBE) {
            // TODO: token 정보를 이용하도록 수정 필요!! 현재는 Authorization에 member ID를 raw data로 넣고 있음.
            String destination = accessor.getDestination();

            if (destination == null) {
                throw new FriendoglyWebSocketException("잘못된 subscribe 요청입니다.");
            }

            if (destination.startsWith(TOPIC_CHAT_ENDPOINT)) {
                validateChatSubscription(accessor, destination);
            }
//            if (destination.startsWith("/topic/invite/")) {
//                validateInviteSubscription(accessor);
//            }
        }
        return message;
    }

    private void validateChatSubscription(StompHeaderAccessor accessor, String destination) {
        String rawMemberId = accessor.getFirstNativeHeader(AUTHORIZATION);

        validateLogin(rawMemberId);
        long memberId = parseMemberId(rawMemberId);
        long chatRoomId = parseChatRoomId(destination);

        validateMemberInChatRoom(memberId, chatRoomId);
    }

    private void validateLogin(String rawMemberId) {
        if (rawMemberId == null) {
            throw new FriendoglyWebSocketException("로그인 후 이용하세요.");
        }
    }

    private long parseMemberId(String rawMemberId) {
        try {
            return Long.parseLong(rawMemberId);
        } catch (NumberFormatException e) {
            throw new FriendoglyWebSocketException("올바르지 않은 토큰 형식입니다.");
        }
    }

    private long parseChatRoomId(String destination) {
        try {
            String rawChatRoomId = destination.substring(TOPIC_CHAT_ENDPOINT.length());
            return Long.parseLong(rawChatRoomId);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new FriendoglyWebSocketException("올바르지 않은 채팅방 ID입니다.");
        }
    }

    private void validateMemberInChatRoom(long memberId, long chatRoomId) {
        if (!chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId)) {
            throw new FriendoglyWebSocketException("채팅방에 입장할 권한이 없습니다.");
        }
    }
}
