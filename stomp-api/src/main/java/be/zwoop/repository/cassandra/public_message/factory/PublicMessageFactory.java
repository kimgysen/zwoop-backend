package be.zwoop.repository.cassandra.public_message.factory;

import be.zwoop.repository.cassandra.public_message.PublicMessageEntity;
import be.zwoop.repository.cassandra.public_message.PublicMessagePrimaryKey;
import be.zwoop.web.dto.receive.PublicMessageReceiveDto;
import be.zwoop.web.dto.send.PublicMessageSendDto;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PublicMessageFactory {

    public PublicMessageEntity buildPublicMessageEntity(String userId, String nickName, String avatar, PublicMessageReceiveDto dto) {
        PublicMessagePrimaryKey pk = PublicMessagePrimaryKey.builder()
                .chatRoomId(dto.getChatRoomId())
                .date(new Date())
                .build();

        return PublicMessageEntity.builder()
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
