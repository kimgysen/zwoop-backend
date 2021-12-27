package be.zwoop.service.chatroom;

import be.zwoop.repository.cassandra.PrivateMessage;
import be.zwoop.repository.cassandra.PublicMessage;
import be.zwoop.repository.redis.ChatRoom;
import be.zwoop.repository.redis.ChatRoomUser;

import java.util.Optional;

public interface ChatRoomService {

    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findById(String chatRoomId);
    void join(ChatRoom chatRoom, ChatRoomUser joiningUser);
    void leave(ChatRoom chatRoom, ChatRoomUser leavingUser);
    void sendPublicMessage(PublicMessage msg);
    void sendPrivateMessage(PrivateMessage msg);

}
