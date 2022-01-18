package be.zwoop.web;

import be.zwoop.features.inbox.repository.cassandra.InboxItemEntity;
import be.zwoop.features.inbox.service.InboxService;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;
import be.zwoop.features.private_chat.mapper.PrivateMessageMapper;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.security.UserPrincipal;
import be.zwoop.features.private_chat.service.PrivateChatService;
import be.zwoop.web.dto.receive.PrivateMessageReceiveDto;
import be.zwoop.web.dto.send.PartnerReadSendDto;
import be.zwoop.web.dto.send.PrivateMessageSendDto;
import be.zwoop.websocket.service.WsUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static be.zwoop.websocket.keys.SessionKeys.SESSION_POST_ID;


@RestController
@AllArgsConstructor
public class PrivateChatController {
    private final WsUtil wsUtil;
    private final AuthenticationFacade authenticationFacade;
    private final PrivateChatService privateChatService;
    private final PrivateMessageMapper privateMessageMapper;
    private final InboxService inboxService;


    @GetMapping("/app/messages/private/{postId}")
    public Slice<PrivateMessageEntity> getOldPrivateMessages(
            Pageable pageable,
            @PathVariable String postId,
            @RequestParam(value = "partnerId") String chatPartnerUserId,
            @RequestParam(value = "before") Date date) {
        // TODO: Check if chatroom exists
        // TODO: Add validation on required query parameters
        String userId = authenticationFacade.getAuthenticatedUserId().toString();
        return privateChatService.findPrivateMessagesBefore(pageable, postId, userId, chatPartnerUserId, date);
    }

    @SubscribeMapping("/old.private.messages/{partnerId}")
    public List<PrivateMessageSendDto> getOldPrivateMessagesOnSubscribe(
            @DestinationVariable String partnerId,
            SimpMessageHeaderAccessor headerAccessor) {
        String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headerAccessor);
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);

        List<PrivateMessageEntity> privateMessageEntityEntities =
                privateChatService.findFirst20PrivateMessagesByPkPostId(postId, principal.getUsername(), partnerId);
        return privateMessageMapper.mapEntityListToSendDto(privateMessageEntityEntities);
    }

    @SubscribeMapping("/old.private.messages/{partnerId}/read")
    public boolean getPrivateMessagesRead(
            @DestinationVariable String partnerId,
            SimpMessageHeaderAccessor headerAccessor) {
        String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headerAccessor);
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);

        Optional<InboxItemEntity> inboxItemOpt = inboxService.findByPostIdAndUserIdAndPartnerId(postId, principal.getUsername(), partnerId);
        return inboxItemOpt.isPresent();
    }

    @MessageMapping("/send.message.private")
    public void sendPrivateMessage(@Payload PrivateMessageReceiveDto msgDto, SimpMessageHeaderAccessor headerAccessor) {
        String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headerAccessor);
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);

        privateChatService.sendPrivateMessage(postId, principal.getUsername(), principal.getNickName(), principal.getAvatar(), msgDto);
    }

    @MessageMapping("/start.typing/{partnerId}")
    public void startTyping(@DestinationVariable String partnerId, SimpMessageHeaderAccessor headerAccessor) {
        String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headerAccessor);
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);
        privateChatService.startTyping(postId, principal.getUsername(), partnerId);
    }

    @MessageMapping("/stop.typing/{partnerId}")
    public void stopTyping(@DestinationVariable String partnerId, SimpMessageHeaderAccessor headerAccessor) {
        String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headerAccessor);
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);
        privateChatService.stopTyping(postId, principal.getUsername(), partnerId);
    }

}
