package be.zwoop.features.chatroom.mapper;


import be.zwoop.features.chatroom.repository.cassandra.ChatRoomMessageEntity;
import be.zwoop.web.public_chat.dto.send.PublicMessageSendDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class ChatRoomMessageMapper {

    public static PublicMessageSendDto mapEntityToSendDto(ChatRoomMessageEntity pmEntity) {
        return PublicMessageSendDto
                .builder()
                .chatRoomId(pmEntity.getPk().getChatRoomId())
                .date(pmEntity.getPk().getDate())
                .fromUserId(pmEntity.getFromUserId())
                .fromUserNickName(pmEntity.getFromUserNickName())
                .fromUserAvatar(pmEntity.getFromUserAvatar())
                .message(pmEntity.getMessage())
                .build();
    }

    public List<PublicMessageSendDto> mapEntityListToSendDto(List<ChatRoomMessageEntity> entities) {
        return entities
                .stream()
                .map(ChatRoomMessageMapper::mapEntityToSendDto)
                .collect(toList());
    }

}
