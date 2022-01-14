package be.zwoop.features.online;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;


@AllArgsConstructor
@Data
@RedisHash("online_users")
public class OnlineUserRedisEntity {

    @Id
    private String id;

}
