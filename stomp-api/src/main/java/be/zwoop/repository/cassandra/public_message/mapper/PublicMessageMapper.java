package be.zwoop.repository.cassandra.public_message.mapper;


import be.zwoop.repository.cassandra.public_message.PublicMessageEntity;
import be.zwoop.web.dto.send.PublicMessageSendDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class PublicMessageMapper {

    public static PublicMessageSendDto mapEntityToSendDto(PublicMessageEntity pmEntity) {
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

    public List<PublicMessageSendDto> mapEntityListToSendDto(List<PublicMessageEntity> entities) {
        return entities
                .stream()
                .map(PublicMessageMapper::mapEntityToSendDto)
                .collect(toList());
    }

}
