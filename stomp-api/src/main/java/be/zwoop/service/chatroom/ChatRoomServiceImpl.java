package be.zwoop.service.chatroom;

import be.zwoop.repository.cassandra.PrivateMessage;
import be.zwoop.repository.cassandra.PublicMessage;
import be.zwoop.repository.redis.ChatRoom;
import be.zwoop.repository.redis.ChatRoomRepository;
import be.zwoop.repository.redis.ChatRoomUser;
import be.zwoop.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private SimpMessagingTemplate wsTemplate;
    private ChatRoomRepository chatRoomRepository;
    private MessageService messageService;


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
    }

    @Override
    public void leave(ChatRoom chatRoom, ChatRoomUser user) {
        chatRoom.removeUser(user);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public void sendPublicMessage(PublicMessage msg) {
        wsTemplate.convertAndSend(publicMsgDestination(msg.getPk().getChatRoomId()), msg);
        messageService.persistPublicMessage(msg);
    }

    @Override
    public void sendPrivateMessage(PrivateMessage msg) {
        wsTemplate.convertAndSendToUser(
                msg.getFromUserId(), privateMsgDestination(msg.getPk().getChatRoomId()), msg);

        wsTemplate.convertAndSendToUser(
                msg.getToUserId(), privateMsgDestination(msg.getPk().getChatRoomId()), msg);

        messageService.persistPrivateMessage(msg);
    }

    private String publicMsgDestination(String chatRoomId) {
        return "/topic/" + chatRoomId + ".public.messages";
    }

    private String privateMsgDestination(String chatRoomId) {
        return "/queue/" + chatRoomId + ".private.messages";
    }

}
