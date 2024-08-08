package com.woowacourse.friendogly.config;

import static com.woowacourse.friendogly.common.ErrorCode.DEFAULT_ERROR_CODE;
import static org.springframework.messaging.simp.stomp.StompCommand.ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.friendogly.common.ApiResponse;
import com.woowacourse.friendogly.common.ErrorResponse;
import com.woowacourse.friendogly.exception.FriendoglyWebSocketException;
import java.util.Collections;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper objectMapper;

    public WebSocketErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        Throwable causeException = ex.getCause();
        if (causeException instanceof FriendoglyWebSocketException) {
            return createMessageBytes(causeException.getMessage());
        }
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> createMessageBytes(String message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(ERROR);
        ErrorResponse errorResponse = new ErrorResponse(DEFAULT_ERROR_CODE, message, Collections.emptyList());

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(ApiResponse.ofError(errorResponse));
            return MessageBuilder.createMessage(bytes, accessor.getMessageHeaders());
        } catch (JsonProcessingException e) {
            return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
        }
    }
}
