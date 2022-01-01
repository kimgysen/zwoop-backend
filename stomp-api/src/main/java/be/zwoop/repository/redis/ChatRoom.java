package be.zwoop.repository.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@RedisHash("chatrooms")
public class ChatRoom {

    @Id
    private String id;
    private String name;

    private Set<ChatRoomUser> connectedUsers = new HashSet<>();

    public ChatRoom() {}

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
