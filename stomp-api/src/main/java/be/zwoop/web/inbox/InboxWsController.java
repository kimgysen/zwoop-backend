package be.zwoop.web.inbox;

import be.zwoop.features.inbox.repository.cassandra.InboxItemEntity;
import be.zwoop.features.inbox.service.InboxService;
import be.zwoop.security.UserPrincipal;
import be.zwoop.web.private_chat.dto.receive.MarkAsReadDto;
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
public class InboxWsController {

    private final WsUtil wsUtil;
    private final InboxService inboxService;

    @SubscribeMapping("/app.inbox.items")
    public List<InboxItemEntity> getUserInboxItemsOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);

        return inboxService.findFirst20InboxItemsByUserId(principal.getUsername());
    }

    @SubscribeMapping("/post/{postId}/inbox.items")
    public List<InboxItemEntity> getPostInboxItemsOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headerAccessor);
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);

        return inboxService.findAllInboxItemsByPostIdAndUserId(postId, principal.getUsername());
    }

    @MessageMapping("/mark.as.read")
    public void markPrivateMessageAsRead(@Payload MarkAsReadDto markAsReadDto, SimpMessageHeaderAccessor headerAccessor) {
        String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headerAccessor);
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);

        inboxService.markInboxItemAsRead(postId, principal.getUsername(), markAsReadDto.getPartnerId());
        inboxService.markHasPartnerRead(postId, markAsReadDto.getPartnerId(), principal.getUsername());
    }

}
