package be.zwoop.websocket;

import be.zwoop.security.UserPrincipal;
import be.zwoop.websocket.service.ConnectType;
import be.zwoop.websocket.service.WsUtil;
import be.zwoop.websocket.service.disconnect.DisconnectService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static be.zwoop.websocket.keys.SessionKeys.*;


@AllArgsConstructor
@Component
public class DisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    private final WsUtil wsUtil;
    private final DisconnectService disconnectService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        handleChatroomDisconnect(event);
    }

    private void handleChatroomDisconnect(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        UserPrincipal principal = wsUtil.getPrincipal(headers);

        ConnectType connectType = ConnectType.valueOf(wsUtil.getSessionAttr(SESSION_CONNECT_TYPE, headers));

        disconnectService.saveOfflineStatusRedis(principal);

        switch (connectType) {
            case PUBLIC_CHAT -> {
                String chatRoomId = wsUtil.getSessionAttr(SESSION_CHATROOM_ID, headers);
                disconnectService.saveAbsenceStatusPublicChatRoom(chatRoomId, principal);
            }
            case PRIVATE_CHAT -> {
                String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headers);
                disconnectService.saveAbsenceStatusPrivateChat(postId, principal);
            }
        }

    }
}
