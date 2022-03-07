package be.zwoop.features.chatroom.factory;

import be.zwoop.features.chatroom.repository.cassandra.ChatRoomMessageEntity;
import be.zwoop.web.public_chat.dto.receive.PublicMessageReceiveDto;
import be.zwoop.web.public_chat.dto.send.PublicMessageSendDto;

public interface ChatRoomMessageFactory {
    ChatRoomMessageEntity buildPublicMessageEntity(String userId, String nickName, String avatar, PublicMessageReceiveDto dto);
    PublicMessageSendDto buildPublicMessageSendDto(String userId, String nickName, String avatar, PublicMessageReceiveDto receiveDto);
}
