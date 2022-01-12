package be.zwoop.websocket.service.disconnect;

import be.zwoop.security.UserPrincipal;

public interface DisconnectService {
    void saveOfflineStatusRedis(UserPrincipal principal);
    void saveAbsenceStatusPublicChatRoom(String chatRoomId, UserPrincipal principal);
    void saveAbsenceStatusPrivateChat(String postId, UserPrincipal principal);

}
