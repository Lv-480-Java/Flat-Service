package com.softserve.maklertaboo.config;

import com.softserve.maklertaboo.interceptor.HttpHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * Configuration Web Socket into Makler Taboo project.
 *
 * @author Mykola Borovets
 */
@Configuration
@EnableWebSocketMessageBroker
@Component
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private HttpHandshakeInterceptor httpHandshakeInterceptor;

    /**
     * @param httpHandshakeInterceptor
     * @author Mykola Borovets
     */
    @Autowired
    public WebSocketConfig(HttpHandshakeInterceptor httpHandshakeInterceptor) {
        this.httpHandshakeInterceptor = httpHandshakeInterceptor;
    }

    /**
     * @param registry
     * @author Mykola Borovets
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/chat");
    }


    /**
     * @param registry
     * @author Mykola Borovets
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/wss")
                .setAllowedOrigins("https://maklertabooclient.herokuapp.com", "http://localhost:4200")
                .withSockJS()
                .setInterceptors(httpHandshakeInterceptor);
    }
}
