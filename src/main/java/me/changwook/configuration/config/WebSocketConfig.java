package me.changwook.configuration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        //클라이언트가 메시지를 구독할 엔드포인트 prefix설정
        registry.enableSimpleBroker("/sub");

        //서버에 클라이언트 메시지를 받을 엔드포인트 prefix설정
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트에서 웹소켓에 접속할 때 사용할 주소
        // nginx 프록시를 통한 연결을 허용하기 위한 CORS 설정 추가
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*") // nginx 프록시 허용
                .withSockJS();
    }
}
