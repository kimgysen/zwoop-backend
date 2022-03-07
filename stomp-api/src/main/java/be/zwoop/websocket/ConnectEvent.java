package be.zwoop.websocket;

import be.zwoop.security.UserPrincipal;
import be.zwoop.websocket.service.StreamType;
import be.zwoop.websocket.service.WsUtil;
import be.zwoop.websocket.service.connect.ConnectService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import static be.zwoop.websocket.keys.HeaderKeys.*;
import static be.zwoop.websocket.keys.SessionKeys.*;


@AllArgsConstructor
@Component
public class ConnectEvent implements ApplicationListener<SessionConnectEvent> {

    private final WsUtil wsUtil;
    private final ConnectService connectService;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        handleConnect(event);
    }

    private void handleConnect(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        UserPrincipal principal = wsUtil.getPrincipal(headers);
        wsUtil.storePrincipalInSession(principal, headers);

        StreamType streamType = StreamType.valueOf(
                wsUtil.getNativeHeader(HEADER_CONNECT_TYPE, headers));
        wsUtil.storeInSession(SESSION_STREAM_TYPE, streamType.name(), headers);

        connectService.saveOnlineStatusRedis(principal);

        switch (streamType) {
            case TAG_PUBLIC_CHAT -> {
                String chatRoomId = wsUtil.getNativeHeader(HEADER_CHATROOM_ID, headers);
                wsUtil.storeInSession(SESSION_CHATROOM_ID, chatRoomId, headers);
                connectService.savePresenceStatusPublicChatRoom(chatRoomId, principal);
            }
            case POST_PRIVATE_CHAT -> {
                String postId = wsUtil.getNativeHeader(HEADER_POST_ID, headers);
                wsUtil.storeInSession(SESSION_POST_ID, postId, headers);
                connectService.savePresenceStatusPrivateChat("be.zwoop.domain.post-" + postId, principal);
            }
            case POST_INBOX -> {
                String postId = wsUtil.getNativeHeader(HEADER_POST_ID, headers);
                wsUtil.storeInSession(SESSION_POST_ID, postId, headers);
            }
        }
    }
}
