package be.zwoop.web;


import be.zwoop.repository.cassandra.public_message.PublicMessageEntity;
import be.zwoop.repository.cassandra.public_message.mapper.PublicMessageMapper;
import be.zwoop.repository.redis.chatroom.ChatRoomUserRedisEntity;
import be.zwoop.security.UserPrincipal;
import be.zwoop.service.chatroom.ChatRoomService;
import be.zwoop.service.message.MessageService;
import be.zwoop.web.dto.receive.PublicMessageReceiveDto;
import be.zwoop.web.dto.send.PublicMessageSendDto;
import be.zwoop.websocket.service.WsUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
import java.util.Set;

import static be.zwoop.websocket.keys.SessionKeys.SESSION_CHATROOM_ID;

@RestController
@AllArgsConstructor
public class ChatRoomController {

    private final WsUtil wsUtil;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final PublicMessageMapper publicMessageMapper;

    @GetMapping("/chatroom/messages/public/{chatRoomId}")
    public Slice<PublicMessageEntity> getOldPublicMessages(
            Pageable pageable,
            @PathVariable String chatRoomId,
            @RequestParam(value = "before") Date date) {
        // TODO: Check if chatroom exists
        // TODO: Add validation on required query parameters
        return messageService.findPublicMessagesBefore(pageable, chatRoomId, date);
    }

    @SubscribeMapping("/old.public.messages")
    public List<PublicMessageSendDto> getOldPublicMessagesOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = wsUtil.getSessionAttr(SESSION_CHATROOM_ID, headerAccessor);
        List<PublicMessageEntity> publicMessageEntityEntities = messageService.findFirst20PublicMessagesByPkChatRoomId(chatRoomId);
        return publicMessageMapper.mapEntityListToSendDto(publicMessageEntityEntities);
    }

    @SubscribeMapping("/connected.users")
    public Set<ChatRoomUserRedisEntity> listChatRoomConnectedUsersOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = wsUtil.getSessionAttr(SESSION_CHATROOM_ID, headerAccessor);
        return chatRoomService.getConnectedUsers(chatRoomId);
    }

    @MessageMapping("/send.message.public")
    public void sendPublicMessage(@Payload PublicMessageReceiveDto msgDto, SimpMessageHeaderAccessor headerAccessor) {
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);
        chatRoomService.sendPublicMessage(principal.getUsername(), principal.getNickName(), principal.getAvatar(), msgDto);
    }

}
