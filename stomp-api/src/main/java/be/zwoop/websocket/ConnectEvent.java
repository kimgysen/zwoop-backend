package be.zwoop.websocket;

import be.zwoop.repository.redis.ChatRoom;
import be.zwoop.repository.redis.ChatRoomUser;
import be.zwoop.repository.redis.OnlineUser;
import be.zwoop.repository.redis.OnlineUserRepository;
import be.zwoop.security.UserPrincipal;
import be.zwoop.service.chatroom.ChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.Date;
import java.util.Optional;


@AllArgsConstructor
@Component
public class ConnectEvent implements ApplicationListener<SessionConnectEvent> {

    private final ChatRoomService chatRoomService;
    private final OnlineUserRepository onlineUserRepository;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String chatRoomId = headers
                .getNativeHeader("chatRoomId")
                .get(0);
        headers.getSessionAttributes()
                .put("chatRoomId", chatRoomId);

        ChatRoom chatRoom;
        Optional<ChatRoom> chatRoomOpt = chatRoomService.findById(chatRoomId);

        if (chatRoomOpt.isEmpty()) {
            chatRoom = chatRoomService.save(new ChatRoom(chatRoomId, chatRoomId));
        } else {
            chatRoom = chatRoomOpt.get();
        }

        UserPrincipal principal = getPrincipal(headers);
        String userId = principal.getUsername();
        String nickName = principal.getNickName();

        if (userId != null && nickName != null) {
            onlineUserRepository.save(new OnlineUser(userId));

            ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                    .userId(principal.getUsername())
                    .nickName(principal.getNickName())
                    .joinedAt(new Date())
                    .build();
            chatRoomService.join(chatRoom, chatRoomUser);
        }

    }

    private UserPrincipal getPrincipal(SimpMessageHeaderAccessor headers) {
        return (UserPrincipal) headers.getSessionAttributes().get("userPrincipal");
    }

}
