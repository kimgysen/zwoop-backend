package be.zwoop.repository.redis.chatroom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@RedisHash("chatrooms")
public class ChatRoomRedisEntity {

    @Id
    private String id;
    private String name;

    private Set<ChatRoomUserRedisEntity> connectedUsers = new HashSet<>();

    public ChatRoomRedisEntity() {}

    public ChatRoomRedisEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public void addUser(ChatRoomUserRedisEntity user) {
        this.connectedUsers.add(user);
    }

    public void removeUser(ChatRoomUserRedisEntity user) {
        this.connectedUsers.remove(user);
    }

    public int getNumberOfConnectedUsers() {
        return this.connectedUsers.size();
    }

}
