package in.vvm.chat_socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import in.vvm.chat_socket.handler.WebSocketHandler;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer{
    
     @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // In-memory broker for /topic destinations
        config.setApplicationDestinationPrefixes("/chat_socket"); // Prefix for application destinations
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat"); // WebSocket endpoint
        registry.addEndpoint("/chat").withSockJS(); // SockJS fallback for non-WebSocket clients
    }
}
