package be.zwoop.repository.redis.chatroom;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
@Builder
public class ChatRoomUserRedisEntity implements Comparable<ChatRoomUserRedisEntity> {

	private final String userId;
	private final String nickName;
	private final String avatar;
	private final Date joinedAt;


	@Override
	public int compareTo(ChatRoomUserRedisEntity chatRoomUserRedisEntity) {
		return this.userId.compareTo(chatRoomUserRedisEntity.getUserId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChatRoomUserRedisEntity that = (ChatRoomUserRedisEntity) o;
		return userId.equals(that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}
}
