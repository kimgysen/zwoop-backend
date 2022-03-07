package be.zwoop.features.private_chat.factory;

import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;
import be.zwoop.web.private_chat.dto.receive.PrivateMessageReceiveDto;
import be.zwoop.web.private_chat.dto.send.feature.PrivateMessageSendDto;

public interface PrivateMessageFactory {

    PrivateMessageEntity buildPrivateMessage(
            String postId,
            String userId,
            String partnerId,
            String senderId,
            String senderNickName,
            String senderAvatar,
            PrivateMessageReceiveDto dto);

    PrivateMessageSendDto buildPrivateMessageSendDto(
            String postId,
            String userId,
            String partnerId,
            String senderId,
            String senderNickName,
            String senderAvatar,
            PrivateMessageReceiveDto dto);

}

