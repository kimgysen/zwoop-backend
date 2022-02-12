package be.zwoop.features.private_chat.factory;

import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessagePrimaryKey;
import be.zwoop.web.private_chat.dto.receive.PrivateMessageReceiveDto;
import be.zwoop.web.private_chat.dto.send.feature.PrivateMessageSendDto;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PrivateMessageFactory {

    public PrivateMessageEntity buildPrivateMessage(
            String postId,
            String userId, String partnerId,
            String senderId, String senderNickName, String senderAvatar,
            PrivateMessageReceiveDto dto) {
        PrivateMessagePrimaryKey pk = PrivateMessagePrimaryKey.builder()
                .postId(postId)
                .userId(userId)
                .partnerId(partnerId)
                .date(new Date())
                .build();

        return PrivateMessageEntity.builder()
                .pk(pk)
                .fromUserId(senderId)
                .fromNickName(senderNickName)
                .fromAvatar(senderAvatar)
                .toUserId(dto.getToUserId())
                .toNickName(dto.getToUserNickName())
                .toAvatar(dto.getToUserAvatar())
                .message(dto.getMessage())
                .build();
    }

    public PrivateMessageSendDto buildPrivateMessageSendDto(
            String postId,
            String userId, String partnerId,
            String senderId, String senderNickName, String senderAvatar,
            PrivateMessageReceiveDto dto) {
        return PrivateMessageSendDto.builder()
                .postId(postId)
                .userId(userId)
                .partnerId(partnerId)
                .date(new Date())
                .fromUserId(senderId)
                .fromNickName(senderNickName)
                .fromAvatar(senderAvatar)
                .toUserId(dto.getToUserId())
                .toNickName(dto.getToUserNickName())
                .toAvatar(dto.getToUserAvatar())
                .message(dto.getMessage())
                .build();
    }

}

