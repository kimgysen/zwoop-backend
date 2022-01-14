package be.zwoop.web;


import be.zwoop.features.chatroom.repository.cassandra.ChatRoomMessageEntity;
import be.zwoop.features.chatroom.mapper.ChatRoomMessageMapper;
import be.zwoop.features.chatroom.repository.redis.ChatRoomUserRedisEntity;
import be.zwoop.security.UserPrincipal;
import be.zwoop.features.chatroom.service.ChatRoomService;
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
    private final ChatRoomMessageMapper chatRoomMessageMapper;


    @GetMapping("/app/messages/public/{chatRoomId}")
    public Slice<ChatRoomMessageEntity> getOldPublicMessages(
            Pageable pageable,
            @PathVariable String chatRoomId,
            @RequestParam(value = "before") Date date) {
        // TODO: Check if chatroom exists
        // TODO: Add validation on required query parameters
        return chatRoomService.findPublicMessagesBefore(pageable, chatRoomId, date);
    }

    @SubscribeMapping("/chatroom.old.messages")
    public List<PublicMessageSendDto> getOldPublicMessagesOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = wsUtil.getSessionAttr(SESSION_CHATROOM_ID, headerAccessor);
        List<ChatRoomMessageEntity> chatRoomMessageEntityEntities = chatRoomService.findFirst20PublicMessagesByPkChatRoomId(chatRoomId);
        return chatRoomMessageMapper.mapEntityListToSendDto(chatRoomMessageEntityEntities);
    }

    @SubscribeMapping("/chatroom.connected.users")
    public Set<ChatRoomUserRedisEntity> listChatRoomConnectedUsersOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = wsUtil.getSessionAttr(SESSION_CHATROOM_ID, headerAccessor);
        return chatRoomService.getConnectedUsers(chatRoomId);
    }

    @MessageMapping("/send.message.chatroom")
    public void sendPublicMessage(@Payload PublicMessageReceiveDto msgDto, SimpMessageHeaderAccessor headerAccessor) {
        UserPrincipal principal = wsUtil.getPrincipal(headerAccessor);
        chatRoomService.sendPublicMessage(principal.getUsername(), principal.getNickName(), principal.getAvatar(), msgDto);
    }

}
