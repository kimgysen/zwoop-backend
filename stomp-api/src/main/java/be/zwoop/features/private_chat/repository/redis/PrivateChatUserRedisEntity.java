package be.zwoop.features.private_chat.repository.redis;

import com.google.common.base.Objects;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PrivateChatUserRedisEntity implements Comparable<PrivateChatUserRedisEntity>{

    private final String userId;
    private final String nickName;
    private final String avatar;
    private final Date joinedAt;


    @Override
    public int compareTo(PrivateChatUserRedisEntity chatRoomUserRedisEntity) {
        return this.userId.compareTo(chatRoomUserRedisEntity.getUserId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateChatUserRedisEntity that = (PrivateChatUserRedisEntity) o;
        return Objects.equal(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }
}
