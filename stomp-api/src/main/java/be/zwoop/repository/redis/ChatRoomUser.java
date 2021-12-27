package be.zwoop.repository.redis;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ChatRoomUser implements Comparable<ChatRoomUser> {

	private final String userId;
	private final String nickName;
	private final Date joinedAt = new Date();


	@Override
	public int compareTo(ChatRoomUser chatRoomUser) {
		return this.nickName.compareTo(chatRoomUser.getNickName());
	}

}
