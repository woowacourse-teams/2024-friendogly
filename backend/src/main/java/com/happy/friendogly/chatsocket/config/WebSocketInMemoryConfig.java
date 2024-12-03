package com.happy.friendogly.chatsocket.config;

import com.happy.friendogly.auth.WebSocketArgumentResolver;
import com.happy.friendogly.auth.service.jwt.JwtProvider;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketInMemoryConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketInterceptor webSocketInterceptor;
    private final WebSocketErrorHandler webSocketErrorHandler;
    private final JwtProvider jwtProvider;

    public WebSocketInMemoryConfig(
            WebSocketInterceptor webSocketInterceptor,
            WebSocketErrorHandler webSocketErrorHandler,
            JwtProvider jwtProvider
    ) {
        this.webSocketInterceptor = webSocketInterceptor;
        this.webSocketErrorHandler = webSocketErrorHandler;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/exchange");
        registry.setApplicationDestinationPrefixes("/publish");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOrigins("*");
        registry.setErrorHandler(webSocketErrorHandler);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new WebSocketArgumentResolver(jwtProvider));
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketInterceptor);
    }
}
