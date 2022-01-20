package be.zwoop.web;

import be.zwoop.features.inbox.mapper.InboxItemMapper;
import be.zwoop.features.inbox.repository.cassandra.InboxItemEntity;
import be.zwoop.features.inbox.service.InboxService;
import be.zwoop.security.UserPrincipal;
import be.zwoop.web.dto.receive.MarkAsReadDto;
import be.zwoop.web.dto.send.InboxItemSendDto;
import be.zwoop.websocket.service.WsUtil;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static be.zwoop.websocket.keys.SessionKeys.SESSION_POST_ID;

@RestController
@AllArgsConstructor
public class InboxController {

    private final WsUtil wsUtil;
    private final InboxService inboxService;
    private final InboxItemMapper inboxItemMapper;

    @SubscribeMapping("/user.inbox.items")
    public List<InboxItemSendDto> getUserInboxItemsOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);

        List<InboxItemEntity> entities = inboxService.findAllInboxItemsByUserId(principal.getUsername());
        return inboxItemMapper.mapEntityListToSendDto(entities);
    }

    @SubscribeMapping("/post.inbox.items")
    public List<InboxItemSendDto> getPostInboxItemsOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headerAccessor);
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);

        List<InboxItemEntity> entities = inboxService.findAllInboxItemsByPostIdAndUserId(postId, principal.getUsername());
        return inboxItemMapper.mapEntityListToSendDto(entities);
    }

    @MessageMapping("/mark.as.read")
    public void markPrivateMessageAsRead(@Payload MarkAsReadDto markAsReadDto, SimpMessageHeaderAccessor headerAccessor) {
        String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headerAccessor);
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);

        inboxService.markInboxItemAsRead(postId, principal.getUsername(), markAsReadDto.getPartnerId());
        inboxService.markHasPartnerRead(postId, markAsReadDto.getPartnerId(), principal.getUsername());
    }

}
