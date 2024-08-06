package com.woowacourse.friendogly.config;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.messaging.simp.stomp.StompCommand.SUBSCRIBE;

import com.woowacourse.friendogly.exception.FriendoglyWebSocketException;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebSocketInterceptor implements ChannelInterceptor {

    private final MemberRepository memberRepository;

    public WebSocketInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getCommand() == SUBSCRIBE) {
            // TODO: token 정보를 이용하도록 수정 필요!! 현재는 Authorization에 member ID를 raw data로 넣고 있음.
            String rawMemberId = accessor.getFirstNativeHeader(AUTHORIZATION);
            validate(rawMemberId);
        }
        return message;
    }

    private void validate(String rawMemberId) {
        validateLogin(rawMemberId);
        long memberId = parse(rawMemberId);
        validateMemberExist(memberId);
    }

    private void validateLogin(String rawMemberId) {
        if (rawMemberId == null) {
            throw new FriendoglyWebSocketException("로그인 후 이용하세요.");
        }
    }

    private long parse(String rawMemberId) {
        try {
            return Long.parseLong(rawMemberId);
        } catch (NumberFormatException e) {
            throw new FriendoglyWebSocketException("올바르지 않은 토큰 형식입니다.");
        }
    }

    private void validateMemberExist(long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new FriendoglyWebSocketException("존재하지 않는 회원입니다.");
        }
    }
}
