package be.zwoop.service.chatroom;

import be.zwoop.repository.cassandra.PrivateMessage;
import be.zwoop.repository.cassandra.PrivateMessagePrimaryKey;
import be.zwoop.repository.cassandra.PublicMessage;
import be.zwoop.repository.cassandra.PublicMessagePrimaryKey;
import be.zwoop.repository.redis.ChatRoom;
import be.zwoop.repository.redis.ChatRoomRepository;
import be.zwoop.repository.redis.ChatRoomUser;
import be.zwoop.repository.redis.OnlineUserRepository;
import be.zwoop.service.message.MessageService;
import be.zwoop.web.dto.PrivateMessageDto;
import be.zwoop.web.dto.PublicMessageDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@AllArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final SimpMessagingTemplate wsTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final OnlineUserRepository onlineUserRepository;
    private final MessageService messageService;


    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public Optional<ChatRoom> findById(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId);
    }

    @Override
    public void join(ChatRoom chatRoom, ChatRoomUser joiningUser) {
        chatRoom.addUser(joiningUser);
        chatRoomRepository.save(chatRoom);
        updateConnectedUsersViaWebSocket(chatRoom);
    }

    @Override
    public void leave(ChatRoom chatRoom, ChatRoomUser user) {
        chatRoom.removeUser(user);
        chatRoomRepository.save(chatRoom);
        updateConnectedUsersViaWebSocket(chatRoom);
    }

    private void updateConnectedUsersViaWebSocket(ChatRoom chatRoom) {
        wsTemplate.convertAndSend(
                connectedUsersDestination(chatRoom.getId()),
                chatRoom.getConnectedUsers());
    }

    @Override
    public void sendPublicMessage(PublicMessageDto msgDto, String userId, String nickName) {
        PublicMessagePrimaryKey pk = PublicMessagePrimaryKey.builder()
                .chatRoomId(msgDto.getChatRoomId())
                .date(new Date())
                .build();

        PublicMessage msg = PublicMessage.builder()
                .pk(pk)
                .fromUserId(userId)
                .fromUserNickName(nickName)
                .message(msgDto.getMessage())
                .build();

        wsTemplate.convertAndSend(publicMsgDestination(msg.getPk().getChatRoomId()), msg);
        messageService.persistPublicMessage(msg);
    }

    @Override
    public void sendPrivateMessage(PrivateMessageDto msgDto, String userId, String nickName) {
        sendPrivateMessageToSender(msgDto, userId, nickName);
        sendPrivateMessageToReceiver(msgDto, userId, nickName);
    }

    private void sendPrivateMessageToSender(PrivateMessageDto msgDto, String senderId, String senderNickName) {
        PrivateMessagePrimaryKey pk = PrivateMessagePrimaryKey.builder()
                .userId(senderId)
                .chatPartnerId(msgDto.getToUserId())
                .build();

        PrivateMessage msg = PrivateMessage.builder()
                .pk(pk)
                .isRead(true)
                .fromUserId(senderId)
                .fromNickName(senderNickName)
                .toUserId(msgDto.getToUserId())
                .toNickName(msgDto.getToUserNickName())
                .build();

        wsTemplate.convertAndSendToUser(
                senderId, privateMsgDestination(), msg);

        messageService.persistPrivateMessage(msg);
    }

    private void sendPrivateMessageToReceiver(PrivateMessageDto msgDto, String senderId, String senderNickName) {
        PrivateMessagePrimaryKey pk = PrivateMessagePrimaryKey.builder()
                .userId(msgDto.getToUserId())
                .chatPartnerId(senderId)
                .build();

        PrivateMessage msg = PrivateMessage.builder()
                .pk(pk)
                .isRead(false)
                .fromUserId(senderId)
                .fromNickName(senderNickName)
                .toUserId(msgDto.getToUserId())
                .toNickName(msgDto.getToUserNickName())
                .build();

        boolean userIsOnline = onlineUserRepository.existsByUserId(msgDto.getToUserId());

        if (userIsOnline) {
            wsTemplate.convertAndSendToUser(
                    msgDto.getToUserId(), privateMsgDestination(), msg);
        }

        messageService.persistPrivateMessage(msg);

    }

    private String publicMsgDestination(String chatRoomId) {
        return "/topic/" + chatRoomId + ".public.messages";
    }

    private String privateMsgDestination() {
        return "/queue/private.messages";
    }

    private String connectedUsersDestination(String chatRoomId) {
        return "/topic/" + chatRoomId + ".connected.users";
    }

}
