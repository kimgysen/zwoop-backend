package be.zwoop.websocket;

import be.zwoop.repository.redis.ChatRoom;
import be.zwoop.repository.redis.ChatRoomUser;
import be.zwoop.repository.redis.OnlineUser;
import be.zwoop.repository.redis.OnlineUserRepository;
import be.zwoop.service.chatroom.ChatRoomService;
import be.zwoop.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;


@AllArgsConstructor
@Component
public class DisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    private final ChatRoomService chatRoomService;
    private final OnlineUserRepository onlineUserRepository;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());

        if (headers.getSessionAttributes() != null && !headers.getSessionAttributes().isEmpty()) {
            String chatRoomId = headers.getSessionAttributes().get("chatRoomId").toString();
            Optional<ChatRoom> chatRoomOpt = chatRoomService.findById(chatRoomId);
            Principal principal = headers.getUser();

            if (chatRoomOpt.isPresent() && principal != null) {
                String userId = principal.getName();

                if (userId != null) {
                    Optional<OnlineUser> onlineUser = onlineUserRepository.findById(userId);
                    onlineUser.ifPresent(onlineUserRepository::delete);

                    ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                            .userId(userId)
                            .joinedAt(new Date())
                            .build();
                    chatRoomService.leave(chatRoomOpt.get(), chatRoomUser);
                }
            }
        }
    }

}
