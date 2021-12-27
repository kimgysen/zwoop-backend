package be.zwoop.repository.redis;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@RedisHash("chatrooms")
public class ChatRoom {

    @Id
    private final String id;
    private final String name;

    private final List<ChatRoomUser> connectedUsers = new ArrayList<>();

    public ChatRoom(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addUser(ChatRoomUser user) {
        this.connectedUsers.add(user);
    }

    public void removeUser(ChatRoomUser user) {
        this.connectedUsers.remove(user);
    }

    public int getNumberOfConnectedUsers() {
        return this.connectedUsers.size();
    }

}
