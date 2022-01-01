package be.zwoop.repository.redis;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;


@AllArgsConstructor
@Getter
@Setter
@RedisHash("online_users")
public class OnlineUser {

    @Id
    private final String id;

}
