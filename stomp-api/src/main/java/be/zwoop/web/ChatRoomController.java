package be.zwoop.web;


import be.zwoop.repository.cassandra.PrivateMessage;
import be.zwoop.repository.cassandra.PublicMessage;
import be.zwoop.repository.redis.ChatRoom;
import be.zwoop.repository.redis.ChatRoomUser;
import be.zwoop.security.facade.AuthenticationFacade;
import be.zwoop.service.chatroom.ChatRoomService;
import be.zwoop.service.message.MessageService;
import be.zwoop.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private AuthenticationFacade authenticationFacade;
    private ChatRoomService chatRoomService;
    private MessageService messageService;


    @GetMapping("/messages/public/{chatRoomId}")
    public Slice<PublicMessage> getOldPublicMessages(
            Pageable pageable,
            @PathVariable String chatRoomId,
            @RequestParam(value = "before") Date date) {
        // TODO: Check if chatroom exists
        // TODO: Add validation on required query parameters
        return messageService.findPublicMessagesBefore(pageable, chatRoomId, date);
    }

    @GetMapping("/messages/private/{chatRoomId}")
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

    @MessageMapping("/chatroom/{chatRoomId}/join")
    public void joinChatRoom(@DestinationVariable String chatRoomId, Principal principal) {
        Optional<ChatRoom> chatRoomOpt = chatRoomService.findById(chatRoomId);
        // TODO: What if chatroom doesn't exist

        if (chatRoomOpt.isPresent()) {
            UserPrincipal userPrincipal = (UserPrincipal) principal;
            ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                    .userId(userPrincipal.getUsername())
                    .nickName(userPrincipal.getNickName())
                    .build();

            chatRoomService.join(chatRoomOpt.get(), chatRoomUser);
        }
    }

    @MessageMapping("/chatroom/{chatRoomId}/leave")
    public void leaveChatRoom(@DestinationVariable String chatRoomId, Principal principal) {
        Optional<ChatRoom> chatRoomOpt = chatRoomService.findById(chatRoomId);
        if (chatRoomOpt.isPresent()) {
            UserPrincipal userPrincipal = (UserPrincipal) principal;
            ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                    .userId(userPrincipal.getUsername())
                    .nickName(userPrincipal.getNickName())
                    .build();

            chatRoomService.leave(chatRoomOpt.get(), chatRoomUser);
        }
    }

    @MessageMapping("/send.message.private")
    public void sendMessage(@Payload PrivateMessage msg, Principal principal) {
        UserPrincipal userPrincipal = (UserPrincipal) principal;
        msg.setFromUserId(userPrincipal.getUsername());
        msg.setFromNickName(userPrincipal.getNickName());
        chatRoomService.sendPrivateMessage(msg);
    }

}
