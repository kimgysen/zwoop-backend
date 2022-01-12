package be.zwoop.web;

import be.zwoop.repository.cassandra.private_message.PrivateMessageEntity;
import be.zwoop.repository.cassandra.private_message.mapper.PrivateMessageMapper;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.security.UserPrincipal;
import be.zwoop.service.message.MessageService;
import be.zwoop.service.private_chat.PrivateChatService;
import be.zwoop.web.dto.receive.PrivateMessageReceiveDto;
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

import static be.zwoop.websocket.keys.SessionKeys.SESSION_POST_ID;


@RestController
@AllArgsConstructor
public class PrivateChatController {
    private final WsUtil wsUtil;
    private final AuthenticationFacade authenticationFacade;
    private final MessageService messageService;
    private final PrivateChatService privateChatService;
    private final PrivateMessageMapper privateMessageMapper;


    @GetMapping("/chatroom/messages/private/{postId}")
    public Slice<PrivateMessageEntity> getOldPrivateMessages(
            Pageable pageable,
            @PathVariable String postId,
            @RequestParam(value = "partnerId") String chatPartnerUserId,
            @RequestParam(value = "before") Date date) {
        // TODO: Check if chatroom exists
        // TODO: Add validation on required query parameters
        String userId = authenticationFacade.getAuthenticatedUserId().toString();
        return messageService.findPrivateMessagesBefore(pageable, postId, userId, chatPartnerUserId, date);
    }

    @SubscribeMapping("/old.private.messages/{partnerId}")
    public List<PrivateMessageSendDto> getOldPrivateMessagesOnSubscribe(
            @DestinationVariable String partnerId,
            SimpMessageHeaderAccessor headerAccessor) {
        String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headerAccessor);
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);

        List<PrivateMessageEntity> privateMessageEntityEntities =
                messageService.findFirst20PrivateMessagesByPkPostId(postId, principal.getUsername(), partnerId);
        return privateMessageMapper.mapEntityListToSendDto(privateMessageEntityEntities);
    }

    @MessageMapping("/send.message.private")
    public void sendPrivateMessage(@Payload PrivateMessageReceiveDto msgDto, SimpMessageHeaderAccessor headerAccessor) {
        String postId = wsUtil.getSessionAttr(SESSION_POST_ID, headerAccessor);
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);

        privateChatService.sendPrivateMessage(postId, principal.getUsername(), principal.getNickName(), principal.getAvatar(), msgDto);
    }
}
