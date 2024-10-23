package com.happy.friendogly.config;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.messaging.simp.stomp.StompCommand.SUBSCRIBE;

import com.happy.friendogly.auth.service.jwt.JwtProvider;
import com.happy.friendogly.chat.domain.ChatRoom;
import com.happy.friendogly.chat.repository.ChatRoomMemberRepository;
import com.happy.friendogly.chat.repository.ChatRoomRepository;
import com.happy.friendogly.club.repository.ClubRepository;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.exception.FriendoglyWebSocketException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketInterceptor implements ChannelInterceptor {

    private static final String TOPIC_CHAT_ENDPOINT = "/exchange/chat.exchange/room.";
//    private static final String TOPIC_INVITE_ENDPOINT = "/topic/invite/";

    private final ClubRepository clubRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final JwtProvider jwtProvider;

    public WebSocketInterceptor(
            ClubRepository clubRepository,
            ChatRoomRepository chatRoomRepository,
            ChatRoomMemberRepository chatRoomMemberRepository,
            JwtProvider jwtProvider
    ) {
        this.clubRepository = clubRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == SUBSCRIBE) {
            String destination = accessor.getDestination();
            validateDestination(destination);

            String accessToken = accessor.getFirstNativeHeader(AUTHORIZATION);
            long memberId = validateAndExtractMemberIdFrom(accessToken);

            // TODO: 1대1 채팅방 구현할 때 고려하기
//            if (destination.startsWith(TOPIC_INVITE_ENDPOINT)) {
//                validateInviteSubscription(memberId, destination);
//            }

            if (destination.startsWith(TOPIC_CHAT_ENDPOINT)) {
                validateChatSubscription(memberId, destination);
            }
        }
        return message;
    }

    private void validateDestination(String destination) {
        if (destination == null) {
            throw new FriendoglyWebSocketException("subscribe 경로는 null일 수 없습니다.");
        }
        if (destination.startsWith(TOPIC_CHAT_ENDPOINT)) {
            return;
        }
//        if (destination.startsWith(TOPIC_INVITE_ENDPOINT)) {
//            return;
//        }
        throw new FriendoglyWebSocketException(String.format("%s는 올바른 sub 경로가 아닙니다.", destination));
    }

    private long validateAndExtractMemberIdFrom(String accessToken) {
        if (accessToken == null) {
            throw new FriendoglyWebSocketException("액세스 토큰은 null일 수 없습니다.");
        }

        try {
            return Long.parseLong(jwtProvider.validateAndExtract(accessToken));
        } catch (NumberFormatException e) {
            throw new FriendoglyWebSocketException("올바르지 않은 토큰 형식입니다.");
        } catch (FriendoglyException e) {
            throw new FriendoglyWebSocketException(e.getMessage());
        }
    }

//    private void validateInviteSubscription(long memberId, String destination) {
//        String rawMemberIdToSubscribe = destination.substring(TOPIC_INVITE_ENDPOINT.length());
//        long memberIdToSubscribe = convertToLong(rawMemberIdToSubscribe);
//
//        if (memberId != memberIdToSubscribe) {
//            throw new FriendoglyWebSocketException("자신의 초대 endpoint만 구독할 수 있습니다.");
//        }
//    }

    private void validateChatSubscription(long memberId, String destination) {
        String rawChatRoomId = destination.substring(TOPIC_CHAT_ENDPOINT.length());
        long chatRoomId = convertToLong(rawChatRoomId);

        ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);

        if (chatRoom.isPrivateChat()) {
            validateChatParticipation(chatRoomId, memberId);
        }

        if (chatRoom.isGroupChat()) {
            validateClubParticipation(chatRoomId, memberId);
        }
    }

    private long convertToLong(String rawId) {
        try {
            return Long.parseLong(rawId);
        } catch (NumberFormatException e) {
            throw new FriendoglyWebSocketException("식별자는 숫자만 입력 가능합니다.");
        }
    }

    private void validateChatParticipation(long chatRoomId, long memberId) {
        if (!chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId)) {
            throw new FriendoglyWebSocketException("채팅방에 입장할 권한이 없습니다.");
        }
    }

    private void validateClubParticipation(long chatRoomId, long memberId) {
        if (!clubRepository.existsBy(chatRoomId, memberId)) {
            throw new FriendoglyWebSocketException("채팅방에 입장할 권한이 없습니다.");
        }
    }
}
