package lv.on.avalanche.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;

import static lv.on.avalanche.utils.HashUtils.extractUserId;
import static lv.on.avalanche.utils.HashUtils.verifyHash;

@Component
public class TopicSubscriptionInterceptor implements ChannelInterceptor {

    @Value("${telegram.secret}")
    private String secretToken;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
            String topicId = extractTopicId(accessor.getDestination());
            String token = accessor.getNativeHeader("Authorization").get(0);

            if (token == null||!verifyHash(token, secretToken)) {
                throw new IllegalArgumentException("User is not authenticated.");
            }
            String userId=extractUserId(token).getId().toString();
            if(!topicId.equals(userId)){
                throw new IllegalArgumentException("User is not authenticated.");
            }
        }

        return message;
    }

    private String getUserIdFromPrincipal(Principal user) {
        return user.getName();
    }

    private String extractTopicId(String topic) {
        return topic.substring(topic.lastIndexOf("/") + 1);
    }


}