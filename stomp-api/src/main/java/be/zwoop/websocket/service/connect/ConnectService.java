package be.zwoop.websocket.service.connect;

import be.zwoop.security.UserPrincipal;

public interface ConnectService {
    void saveOnlineStatusRedis(UserPrincipal userPrincipal);
    void savePresenceStatusPublicChatRoom(String chatRoomId, UserPrincipal principal);
    void savePresenceStatusPrivateChat(String chatRoomId, UserPrincipal principal);

}
