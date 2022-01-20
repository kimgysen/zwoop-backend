package be.zwoop.features.private_chat.service;


import be.zwoop.features.inbox.service.InboxService;
import be.zwoop.features.private_chat.factory.PrivateMessageFactory;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageRepository;
import be.zwoop.features.private_chat.repository.redis.*;
import be.zwoop.web.dto.receive.PrivateMessageReceiveDto;
import be.zwoop.web.dto.send.TypingDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class PrivateChatServiceImpl implements PrivateChatService{

    private final SimpMessagingTemplate wsTemplate;
    private final PrivateChatRepository privateChatRepository;
    private final InboxService inboxService;
    private final PrivateMessageFactory privateMessageFactory;
    private final PrivateMessageRepository privateMessageRepository;
    private final PrivateChatWritingRedisRepository privateChatWritingRedisRepository;

    @Override
    public PrivateChatRedisEntity getOrCreatePrivateChatRedisEntity(String postId) {
        PrivateChatRedisEntity privateChatRedisEntity;
        Optional<PrivateChatRedisEntity> privateChatRedisEntityOpt = findById(postId);

        if (privateChatRedisEntityOpt.isEmpty()) {
            privateChatRedisEntity = privateChatRepository.save(new PrivateChatRedisEntity(postId, postId));
        } else {
            privateChatRedisEntity = privateChatRedisEntityOpt.get();
        }

        return privateChatRedisEntity;
    }

    @Override
    public Optional<PrivateChatRedisEntity> findById(String postId) {
        return privateChatRepository.findById(postId);
    }

    @Override
    public void join(PrivateChatRedisEntity privateChatRedisEntity, PrivateChatUserRedisEntity joiningUser) {
        privateChatRedisEntity.addUser(joiningUser);
        privateChatRepository.save(privateChatRedisEntity);
    }

    @Override
    public void leave(PrivateChatRedisEntity privateChatRedisEntity, PrivateChatUserRedisEntity leavingUser) {
        privateChatRedisEntity.removeUser(leavingUser);
        privateChatRepository.save(privateChatRedisEntity);
    }

    @Override
    public void startTyping(String postId, String userId, String partnerId) {
        Optional<PrivateChatWritingRedisEntity> writingUserOpt = privateChatWritingRedisRepository.findById(userId);
        if (writingUserOpt.isPresent()) {
            PrivateChatWritingRedisEntity writingUser = writingUserOpt.get();
            writingUser.addPartner(
                    WritingToUserRedisEntity.builder()
                            .postId(postId)
                            .partnerId(partnerId)
                            .build());
            privateChatWritingRedisRepository.save(writingUser);
        } else {
            PrivateChatWritingRedisEntity newWritingUser = PrivateChatWritingRedisEntity
                    .builder()
                    .id(userId)
                    .build();
            newWritingUser.addPartner(WritingToUserRedisEntity.builder()
                    .postId(postId)
                    .partnerId(partnerId)
                    .build());
            privateChatWritingRedisRepository.save(newWritingUser);
        }

        wsTemplate.convertAndSendToUser(
                partnerId,
                startTypingDestination(),
                TypingDto.builder()
                        .postId(postId)
                        .partnerId(userId)
                        .build()
        );
    }

    @Override
    public void stopTyping(String postId, String userId, String partnerId) {
        Optional<PrivateChatWritingRedisEntity> writingUserOpt = privateChatWritingRedisRepository.findById(userId);
        if (writingUserOpt.isPresent()) {
            PrivateChatWritingRedisEntity writingUser = writingUserOpt.get();
            writingUser.removePartner(
                WritingToUserRedisEntity.builder()
                    .postId(postId)
                    .partnerId(partnerId)
                    .build());
            privateChatWritingRedisRepository.save(writingUser);
        }

        wsTemplate.convertAndSendToUser(
                partnerId,
                stopTypingDestination(),
                TypingDto.builder()
                        .postId(postId)
                        .partnerId(userId)
                        .build()
        );
    }

    @Override
    public void stopAllTypingForUser(String postId, String userId) {
        Optional<PrivateChatWritingRedisEntity> writingUserOpt = privateChatWritingRedisRepository.findById(userId);
        if (writingUserOpt.isPresent()) {
            PrivateChatWritingRedisEntity writingUser = writingUserOpt.get();
            if (writingUser.getIsWritingToUsers() != null) {
                writingUser.getIsWritingToUsers()
                        .forEach(partner -> {
                            wsTemplate.convertAndSendToUser(
                                    partner.getPartnerId(),
                                    stopTypingDestination(),
                                    TypingDto.builder()
                                            .postId(postId)
                                            .partnerId(userId)
                                            .build());
                        });
                writingUser.removeAllPartners();
                privateChatWritingRedisRepository.save(writingUser);
            }
        }
    }

    @Override
    public void sendPrivateMessage(String postId, String userId, String nickName, String avatar, PrivateMessageReceiveDto msgReceiveDto) {
        sendPrivateMessageToSender(postId, userId, nickName, avatar, msgReceiveDto);
        sendPrivateMessageToReceiver(postId, userId, nickName, avatar, msgReceiveDto);
    }

    private void sendPrivateMessageToSender(String postId, String senderId, String senderNickName, String senderAvatar, PrivateMessageReceiveDto msgReceiveDto) {
        wsTemplate.convertAndSendToUser(
                senderId,
                privateMsgDestination(),
                privateMessageFactory.buildPrivateMessageSendDto(postId, senderId, msgReceiveDto.getToUserId(), senderId, senderNickName, senderAvatar, msgReceiveDto)
        );

        PrivateMessageEntity msgEntity = privateMessageFactory.buildPrivateMessage(
                postId, senderId, msgReceiveDto.getToUserId(), senderId, senderNickName, senderAvatar, msgReceiveDto);
        privateMessageRepository.save(msgEntity);
        inboxService.persistInboxItemForUser(msgEntity, senderId, msgReceiveDto.getToUserId(), true);
    }

    @Override
    public List<PrivateMessageEntity> findFirst20PrivateMessagesByPkPostId(String postId, String userId, String partnerId) {
        return privateMessageRepository.findFirst20ByPkPostIdAndPkUserIdEqualsAndPkPartnerIdEqualsOrderByPkDateDesc(postId, userId, partnerId);
    }

    // TODO: incorrect query
    @Override
    public Slice<PrivateMessageEntity> findPrivateMessagesBefore(Pageable pageable, String postId, String userId, String chatPartnerUserId, Date date) {
        return privateMessageRepository.findAllByPkUserIdEqualsAndFromUserIdEqualsOrToUserIdEqualsAndPkDateGreaterThan(pageable, userId, chatPartnerUserId, chatPartnerUserId, date);
    }

    private void sendPrivateMessageToReceiver(String postId, String senderId, String senderNickName, String senderAvatar, PrivateMessageReceiveDto msgReceiveDto) {
        boolean isReceiverConnected = isUserConnected(postId, msgReceiveDto.getToUserId());

        wsTemplate.convertAndSendToUser(
                msgReceiveDto.getToUserId(),
                privateMsgDestination(),
                privateMessageFactory.buildPrivateMessageSendDto(postId, senderId, msgReceiveDto.getToUserId(), senderId, senderNickName, senderAvatar, msgReceiveDto)
        );

        PrivateMessageEntity msgEntity = privateMessageFactory.buildPrivateMessage(
                postId, msgReceiveDto.getToUserId(), senderId, senderId, senderNickName, senderAvatar, msgReceiveDto);
        privateMessageRepository.save(msgEntity);
        inboxService.persistInboxItemForUser(msgEntity, msgReceiveDto.getToUserId(), senderId, isReceiverConnected);
    }

    @Override
    public boolean isUserConnected(String postId, String userId) {
        Optional<PrivateChatRedisEntity> postOpt = findById(postId);

        return postOpt.map(chatRoomRedisEntity ->
                chatRoomRedisEntity
                        .getConnectedUsers()
                        .stream()
                        .anyMatch(u -> u.getUserId().equals(userId)))
                .orElse(false);
    }

    @Override
    public boolean isPartnerWriting(String postId, String userId, String partnerId) {
        Optional<PrivateChatWritingRedisEntity> writingUserOpt = privateChatWritingRedisRepository.findById(partnerId);
        return writingUserOpt
                .map(privateChatWritingRedisEntity ->
                        privateChatWritingRedisEntity.containsPartner(postId, userId))
                .orElse(false);
    };

    private String privateMsgDestination() {
        return "/exchange/amq.direct/private.messages";
    }

    private String startTypingDestination() {
        return "/exchange/amq.direct/start.typing";
    }

    private String stopTypingDestination() {
        return "/exchange/amq.direct/stop.typing";
    }

}
