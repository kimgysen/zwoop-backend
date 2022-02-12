package be.zwoop.features.chatroom.factory;

import be.zwoop.features.chatroom.repository.cassandra.ChatRoomMessageEntity;
import be.zwoop.features.chatroom.repository.cassandra.ChatRoomMessagePrimaryKey;
import be.zwoop.web.public_chat.dto.receive.PublicMessageReceiveDto;
import be.zwoop.web.public_chat.dto.send.PublicMessageSendDto;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ChatRoomMessageFactory {

    public ChatRoomMessageEntity buildPublicMessageEntity(String userId, String nickName, String avatar, PublicMessageReceiveDto dto) {
        ChatRoomMessagePrimaryKey pk = ChatRoomMessagePrimaryKey.builder()
                .chatRoomId(dto.getChatRoomId())
                .date(new Date())
                .build();

        return ChatRoomMessageEntity.builder()
                .pk(pk)
                .fromUserId(userId)
                .fromUserNickName(nickName)
                .fromUserAvatar(avatar)
                .message(dto.getMessage())
                .build();

    }

    public PublicMessageSendDto buildPublicMessageSendDto(String userId, String nickName, String avatar, PublicMessageReceiveDto receiveDto) {
        return PublicMessageSendDto
                .builder()
                .chatRoomId(receiveDto.getChatRoomId())
                .message(receiveDto.getMessage())
                .fromUserId(userId)
                .fromUserNickName(nickName)
                .fromUserAvatar(avatar)
                .date(new Date())
                .build();
    }

}
