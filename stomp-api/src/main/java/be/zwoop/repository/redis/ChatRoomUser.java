package be.zwoop.repository.redis;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
@Builder
public class ChatRoomUser implements Comparable<ChatRoomUser> {

	private final String userId;
	private final String nickName;
	private final Date joinedAt;


	@Override
	public int compareTo(ChatRoomUser chatRoomUser) {
		return this.userId.compareTo(chatRoomUser.getUserId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChatRoomUser that = (ChatRoomUser) o;
		return userId.equals(that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}
}
