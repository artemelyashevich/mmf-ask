package com.elyashevich.mmfask.security;

import com.elyashevich.mmfask.util.TokenUtil;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NotNull MessageChannel channel) {
        var accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            var token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                var email = TokenUtil.extractEmailClaims(token);
                var auth = new UsernamePasswordAuthenticationToken(
                        email, null, TokenUtil.getRoles(token).stream().map(SimpleGrantedAuthority::new).toList()
                );
                accessor.setUser(auth);
            }
        }
        return message;
    }
}