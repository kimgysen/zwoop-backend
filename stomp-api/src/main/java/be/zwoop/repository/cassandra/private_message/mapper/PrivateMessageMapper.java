package be.zwoop.repository.cassandra.private_message.mapper;


import be.zwoop.repository.cassandra.private_message.PrivateMessageEntity;
import be.zwoop.web.dto.send.PrivateMessageSendDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class PrivateMessageMapper {

    public static PrivateMessageSendDto mapEntityToSendDto(PrivateMessageEntity pmEntity) {
        return PrivateMessageSendDto
                .builder()
                .postId(pmEntity.getPk().getPostId())
                .userId(pmEntity.getPk().getUserId())
                .partnerId(pmEntity.getPk().getPartnerId())
                .date(pmEntity.getPk().getDate())
                .fromUserId(pmEntity.getFromUserId())
                .fromNickName(pmEntity.getFromNickName())
                .fromAvatar(pmEntity.getFromAvatar())
                .toUserId(pmEntity.getToUserId())
                .toNickName(pmEntity.getToNickName())
                .toAvatar(pmEntity.getToAvatar())
                .message(pmEntity.getMessage())
                .build();
    }

    public List<PrivateMessageSendDto> mapEntityListToSendDto(List<PrivateMessageEntity> entities) {
        return entities
                .stream()
                .map(PrivateMessageMapper::mapEntityToSendDto)
                .collect(toList());
    }

}
