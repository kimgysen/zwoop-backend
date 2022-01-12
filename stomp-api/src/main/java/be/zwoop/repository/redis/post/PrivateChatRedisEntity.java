package be.zwoop.repository.redis.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RedisHash("private_chat")
public class PrivateChatRedisEntity {

    @Id
    private String id;
    private String name;

    private Set<PrivateChatUserRedisEntity> connectedUsers = new HashSet<>();

    public PrivateChatRedisEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addUser(PrivateChatUserRedisEntity user) {
        this.connectedUsers.add(user);
    }

    public void removeUser(PrivateChatUserRedisEntity user) {
        this.connectedUsers.remove(user);
    }

    public int getNumberOfConnectedUsers() {
        return this.connectedUsers.size();
    }

}
