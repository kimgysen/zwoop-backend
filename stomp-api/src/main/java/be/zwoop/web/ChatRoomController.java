package be.zwoop.web;


import be.zwoop.repository.cassandra.PrivateMessage;
import be.zwoop.repository.cassandra.PublicMessage;
import be.zwoop.repository.redis.ChatRoom;
import be.zwoop.repository.redis.ChatRoomUser;
import be.zwoop.security.facade.AuthenticationFacade;
import be.zwoop.service.chatroom.ChatRoomService;
import be.zwoop.service.message.MessageService;
import be.zwoop.security.UserPrincipal;
import be.zwoop.web.dto.PrivateMessageDto;
import be.zwoop.web.dto.PublicMessageDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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

import java.security.Principal;
import java.util.*;

@RestController
@AllArgsConstructor
public class ChatRoomController {

    private final AuthenticationFacade authenticationFacade;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;


    @GetMapping("/chatroom/messages/public/{chatRoomId}")
    public Slice<PublicMessage> getOldPublicMessages(
            Pageable pageable,
            @PathVariable String chatRoomId,
            @RequestParam(value = "before") Date date) {
        // TODO: Check if chatroom exists
        // TODO: Add validation on required query parameters
        return messageService.findPublicMessagesBefore(pageable, chatRoomId, date);
    }

    @GetMapping("/chatroom/messages/private/{chatRoomId}")
    public Slice<PrivateMessage> getOldPrivateMessages(
            Pageable pageable,
            @PathVariable String chatRoomId,
            @RequestParam(value = "chatPartnerUserId") String chatPartnerUserId,
            @RequestParam(value = "before") Date date) {
        // TODO: Check if chatroom exists
        // TODO: Add validation on required query parameters
        String userId = authenticationFacade.getAuthenticatedUserId().toString();
        return messageService.findPrivateMessagesBefore(pageable, chatRoomId, userId, chatPartnerUserId, date);
    }


    @SubscribeMapping("/old.public.messages")
    public List<PublicMessage> getOldPublicMessagesOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = headerAccessor
                .getSessionAttributes()
                .get("chatRoomId")
                .toString();

        return messageService.findFirst20ByPkChatRoomId(chatRoomId);
    }

    @SubscribeMapping("/connected.users")
    public Set<ChatRoomUser> listChatRoomConnectedUsersOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = headerAccessor
                .getSessionAttributes()
                .get("chatRoomId")
                .toString();

        Optional<ChatRoom> chatRoomOpt = chatRoomService.findById(chatRoomId);

        if (chatRoomOpt.isPresent()) {
            return chatRoomOpt.get()
                    .getConnectedUsers();

        } else {
            return new HashSet<>();

        }
    }

    @MessageMapping("/send.message.public")
    public void sendPublicMessage(@Payload PublicMessageDto msgDto, SimpMessageHeaderAccessor headerAccessor) {
        UserPrincipal principal = getPrincipal(headerAccessor);
        chatRoomService.sendPublicMessage(msgDto, principal.getUsername(), principal.getNickName());
    }

    @MessageMapping("/send.message.private")
    public void sendPrivateMessage(@Payload PrivateMessageDto msgDto, SimpMessageHeaderAccessor headerAccessor) {
        UserPrincipal principal = getPrincipal(headerAccessor);
        chatRoomService.sendPrivateMessage(msgDto, principal.getUsername(), principal.getNickName());
    }

    private UserPrincipal getPrincipal(SimpMessageHeaderAccessor headerAccessor) {
        return (UserPrincipal) headerAccessor
                .getSessionAttributes()
                .get("userPrincipal");
    }

}
