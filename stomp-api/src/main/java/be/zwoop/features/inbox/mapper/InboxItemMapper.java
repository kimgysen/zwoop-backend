package be.zwoop.features.inbox.mapper;


import be.zwoop.features.inbox.repository.cassandra.InboxItemEntity;
import be.zwoop.web.dto.send.InboxItemSendDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class InboxItemMapper {

    public static InboxItemSendDto mapEntityToSendDto(InboxItemEntity entity) {
        return InboxItemSendDto.builder()
                .postId(entity.getPk().getPostId())
                .userId((entity.getPk().getUserId()))
                .partnerId(entity.getPartnerId())
                .fromUserId(entity.getFromUserId())
                .fromNickName(entity.getFromNickName())
                .fromAvatar(entity.getFromAvatar())
                .toUserId(entity.getToUserId())
                .toNickName(entity.getToNickName())
                .toAvatar(entity.getToAvatar())
                .unread(entity.getUnread())
                .hasPartnerRead(entity.isHasPartnerRead())
                .lastMessage(entity.getLastMessage())
                .lastMessageDate(entity.getPk().getLastMessageDate())
                .build();
    }

    public List<InboxItemSendDto> mapEntityListToSendDto(List<InboxItemEntity> entities) {
        return entities
                .stream()
                .map(InboxItemMapper::mapEntityToSendDto)
                .collect(toList());
    }

}
