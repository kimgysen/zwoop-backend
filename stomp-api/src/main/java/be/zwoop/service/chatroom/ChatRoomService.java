package be.zwoop.service.chatroom;

import be.zwoop.repository.redis.ChatRoom;
import be.zwoop.repository.redis.ChatRoomUser;
import be.zwoop.web.dto.PrivateMessageDto;
import be.zwoop.web.dto.PublicMessageDto;

import java.util.Optional;

public interface ChatRoomService {

    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findById(String chatRoomId);
    void join(ChatRoom chatRoom, ChatRoomUser joiningUser);
    void leave(ChatRoom chatRoom, ChatRoomUser leavingUser);
    void sendPublicMessage(PublicMessageDto msg, String userId, String nickName);
    void sendPrivateMessage(PrivateMessageDto msg, String userId, String nickName);

}
