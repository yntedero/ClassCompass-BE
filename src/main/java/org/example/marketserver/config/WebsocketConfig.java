package org.example.marketserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.marketserver.models.User;
import org.example.marketserver.security.core.CustomAuthentication;
import org.example.marketserver.security.entities.TokenEntity;
import org.example.marketserver.security.repositories.TokenRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
@Slf4j
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

//    private final JwtTokenUtil jwtTokenUtil;
//    private final UserDetailsServiceImpl userDetailsService;
    private final TokenRepository tokenRepository;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
        registry.addEndpoint("/ws");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/user");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                log.info("Headers: {}", accessor);

                assert accessor != null;
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
                    assert authorizationHeader != null;
                    if ( !StringUtils.hasLength(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
                        throw new AuthenticationCredentialsNotFoundException("Authentication failed!");
                    }
                    String token = authorizationHeader.substring("Bearer".length()).trim();
//                    String token = authorizationHeader.substring(7);
//                    String username = jwtTokenUtil.getUsername(token);
                    Optional<TokenEntity> optionalToken = tokenRepository.findByToken(token);

                    if (optionalToken.isEmpty()) {
                        throw new AuthenticationCredentialsNotFoundException("Authentication failed!");
                    }

//                    validateTokenExpiration(optionalToken.get());

                    User user = optionalToken.get().getUser();
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    String userRole = user.getRole();
                    Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userRole));

                    CustomAuthentication auth
                            = new CustomAuthentication(user.getId(), user.getId(), null, authorities, user.getEmail());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("this is auth when message was sent" + auth);
                    accessor.setUser(auth);
                }

                return message;
            }

        });
    }
}
