package be.zwoop.websocket.service.connect;

import be.zwoop.security.UserPrincipal;

public interface ConnectService {
    void saveOnlineStatusRedis(UserPrincipal userPrincipal);
    void savePresenceStatusPublicChatRoom(String chatRoomId, UserPrincipal principal);
    void savePresenceStatusPrivateChat(String postId, UserPrincipal principal);

    boolean isConnectedToChatRoom(String chatRoomId);
    boolean isConnectedToPrivateChat(String postId);


}
