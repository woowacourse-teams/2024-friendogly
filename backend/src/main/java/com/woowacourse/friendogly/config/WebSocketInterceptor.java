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
    private static final String TOPIC_INVITE_ENDPOINT = "/topic/invite/";

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

            if (destination.startsWith(TOPIC_INVITE_ENDPOINT)) {
                validateInviteSubscription(accessor, destination);
            }
            if (destination.startsWith(TOPIC_CHAT_ENDPOINT)) {
                validateChatSubscription(accessor, destination);
            }
        }
        return message;
    }

    private void validateInviteSubscription(StompHeaderAccessor accessor, String destination) {
        String rawMemberId = accessor.getFirstNativeHeader(AUTHORIZATION);
        validateLogin(rawMemberId);
        long memberId = convertToLong(rawMemberId);

        String rawMemberIdToSubscribe = destination.substring(TOPIC_INVITE_ENDPOINT.length());
        long memberIdToSubscribe = convertToLong(rawMemberIdToSubscribe);

        if (memberId != memberIdToSubscribe) {
            throw new FriendoglyWebSocketException("자신의 초대 endpoint만 구독할 수 있습니다.");
        }
    }

    private void validateChatSubscription(StompHeaderAccessor accessor, String destination) {
        String rawMemberId = accessor.getFirstNativeHeader(AUTHORIZATION);
        validateLogin(rawMemberId);
        long memberId = convertToLong(rawMemberId);

        String rawChatRoomId = destination.substring(TOPIC_CHAT_ENDPOINT.length());
        long chatRoomId = convertToLong(rawChatRoomId);

        if (!chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId)) {
            throw new FriendoglyWebSocketException("채팅방에 입장할 권한이 없습니다.");
        }
    }

    private void validateLogin(String rawMemberId) {
        if (rawMemberId == null) {
            throw new FriendoglyWebSocketException("로그인 후 이용하세요.");
        }
    }

    private long convertToLong(String rawId) {
        try {
            return Long.parseLong(rawId);
        } catch (NumberFormatException e) {
            throw new FriendoglyWebSocketException("식별자는 숫자만 입력 가능합니다.");
        }
    }
}
