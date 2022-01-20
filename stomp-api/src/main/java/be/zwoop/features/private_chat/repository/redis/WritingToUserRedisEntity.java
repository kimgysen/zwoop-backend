package be.zwoop.features.private_chat.repository.redis;


import com.google.common.base.Objects;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WritingToUserRedisEntity {
    private final String postId;
    private final String partnerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WritingToUserRedisEntity that = (WritingToUserRedisEntity) o;
        return Objects.equal(postId, that.postId) && Objects.equal(partnerId, that.partnerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(postId, partnerId);
    }
}
