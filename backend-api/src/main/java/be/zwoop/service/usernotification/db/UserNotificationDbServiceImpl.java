package be.zwoop.service.usernotification.db;


import be.zwoop.domain.enum_type.NotificationTypeEnum;
import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.notificationtype.NotificationTypeEntity;
import be.zwoop.repository.notificationtype.NotificationTypeRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.usernotification.UserNotificationEntity;
import be.zwoop.repository.usernotification.UserNotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class UserNotificationDbServiceImpl implements UserNotificationDbService {

    private final NotificationTypeRepository notificationTypeRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final UserNotificationCountDbService userNotificationCountDbService;

    @Override
    @Transactional
    public UserNotificationEntity saveBiddingAddedNotification(BiddingEntity biddingEntity) {
        PostEntity postEntity = biddingEntity.getPost();
        NotificationTypeEntity notificationTypeEntity = notificationTypeRepository.findByNotificationTypeId(NotificationTypeEnum.BIDDING_ADDED.getValue());
        UserNotificationEntity userNotificationEntity = getPostNotificationBuilder(postEntity)
                .notificationType(notificationTypeEntity)
                .sender(biddingEntity.getConsultant())
                .receiver(postEntity.getOp())
                .build();
        userNotificationRepository.saveAndFlush(userNotificationEntity);
        userNotificationCountDbService.incrementUnreadCount(biddingEntity.getPost().getOp());
        return userNotificationEntity;
    }

    @Override
    @Transactional
    public UserNotificationEntity saveBiddingChangedNotification(BiddingEntity biddingEntity) {
        PostEntity postEntity = biddingEntity.getPost();
        NotificationTypeEntity notificationTypeEntity = notificationTypeRepository.findByNotificationTypeId(NotificationTypeEnum.BIDDING_CHANGED.getValue());
        UserNotificationEntity userNotificationEntity = getPostNotificationBuilder(postEntity)
                .notificationType(notificationTypeEntity)
                .sender(biddingEntity.getConsultant())
                .receiver(postEntity.getOp())
                .build();
        userNotificationRepository.saveAndFlush(userNotificationEntity);
        userNotificationCountDbService.incrementUnreadCount(postEntity.getOp());
        return userNotificationEntity;
    }

    @Override
    @Transactional
    public UserNotificationEntity saveBiddingRemoveNotification(BiddingEntity biddingEntity) {
        PostEntity postEntity = biddingEntity.getPost();
        NotificationTypeEntity notificationTypeEntity = notificationTypeRepository.findByNotificationTypeId(NotificationTypeEnum.BIDDING_REMOVED.getValue());
        UserNotificationEntity userNotificationEntity = getPostNotificationBuilder(postEntity)
                .notificationType(notificationTypeEntity)
                .sender(biddingEntity.getConsultant())
                .receiver(postEntity.getOp())
                .build();
        userNotificationRepository.saveAndFlush(userNotificationEntity);
        userNotificationCountDbService.incrementUnreadCount(biddingEntity.getPost().getOp());
        return userNotificationEntity;
    }

    @Override
    @Transactional
    public UserNotificationEntity saveDealInitNotification(DealEntity dealEntity) {
        PostEntity postEntity = dealEntity.getBidding().getPost();
        NotificationTypeEntity notificationTypeEntity = notificationTypeRepository.findByNotificationTypeId(NotificationTypeEnum.DEAL_INIT.getValue());
        UserNotificationEntity userNotificationEntity = getPostNotificationBuilder(postEntity)
                .notificationType(notificationTypeEntity)
                .sender(postEntity.getOp())
                .receiver(dealEntity.getBidding().getConsultant())
                .build();
        userNotificationRepository.saveAndFlush(userNotificationEntity);
        userNotificationCountDbService.incrementUnreadCount(dealEntity.getBidding().getConsultant());
        return userNotificationEntity;
    }

    @Override
    @Transactional
    public UserNotificationEntity saveDealCancelledNotification(DealEntity dealEntity) {
        PostEntity postEntity = dealEntity.getBidding().getPost();
        NotificationTypeEntity notificationTypeEntity = notificationTypeRepository.findByNotificationTypeId(NotificationTypeEnum.DEAL_CANCELLED.getValue());
        UserNotificationEntity userNotificationEntity = getPostNotificationBuilder(postEntity)
                .notificationType(notificationTypeEntity)
                .sender(postEntity.getOp())
                .receiver(dealEntity.getBidding().getConsultant())
                .build();
        userNotificationRepository.saveAndFlush(userNotificationEntity);
        userNotificationCountDbService.incrementUnreadCount(dealEntity.getBidding().getConsultant());
        return userNotificationEntity;
    }

    @Override
    @Transactional
    public UserNotificationEntity saveAnswerAddedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();
        NotificationTypeEntity notificationTypeEntity = notificationTypeRepository.findByNotificationTypeId(NotificationTypeEnum.ANSWER_ADDED.getValue());
        UserNotificationEntity userNotificationEntity = getPostNotificationBuilder(postEntity)
                .notificationType(notificationTypeEntity)
                .sender(answerEntity.getConsultant())
                .receiver(postEntity.getOp())
                .build();
        userNotificationRepository.saveAndFlush(userNotificationEntity);
        userNotificationCountDbService.incrementUnreadCount(answerEntity.getPost().getOp());
        return userNotificationEntity;
    }

    @Override
    @Transactional
    public UserNotificationEntity saveAnswerChangedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();
        NotificationTypeEntity notificationTypeEntity = notificationTypeRepository.findByNotificationTypeId(NotificationTypeEnum.ANSWER_CHANGED.getValue());
        UserNotificationEntity userNotificationEntity = getPostNotificationBuilder(postEntity)
                .notificationType(notificationTypeEntity)
                .sender(answerEntity.getConsultant())
                .receiver(postEntity.getOp())
                .build();
        userNotificationRepository.saveAndFlush(userNotificationEntity);
        userNotificationCountDbService.incrementUnreadCount(postEntity.getOp());
        return userNotificationEntity;
    }

    @Override
    @Transactional
    public UserNotificationEntity saveAnswerRemovedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();
        NotificationTypeEntity notificationTypeEntity = notificationTypeRepository.findByNotificationTypeId(NotificationTypeEnum.ANSWER_REMOVED.getValue());
        UserNotificationEntity userNotificationEntity = getPostNotificationBuilder(postEntity)
                .notificationType(notificationTypeEntity)
                .sender(answerEntity.getConsultant())
                .receiver(answerEntity.getPost().getOp())
                .build();
        userNotificationRepository.saveAndFlush(userNotificationEntity);
        userNotificationCountDbService.incrementUnreadCount(postEntity.getOp());
        return userNotificationEntity;
    }

    @Override
    @Transactional
    public UserNotificationEntity saveAnswerAcceptedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();
        NotificationTypeEntity notificationTypeEntity = notificationTypeRepository.findByNotificationTypeId(NotificationTypeEnum.ANSWER_ACCEPTED.getValue());
        UserNotificationEntity userNotificationEntity = getPostNotificationBuilder(postEntity)
                .notificationType(notificationTypeEntity)
                .sender(answerEntity.getConsultant())
                .receiver(postEntity.getOp())
                .build();
        userNotificationRepository.saveAndFlush(userNotificationEntity);
        userNotificationCountDbService.incrementUnreadCount(postEntity.getOp());
        return userNotificationEntity;
    }

    @Override
    @Transactional
    public UserNotificationEntity saveAnswerPaidNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();
        NotificationTypeEntity notificationTypeEntity = notificationTypeRepository.findByNotificationTypeId(NotificationTypeEnum.PAID.getValue());
        UserNotificationEntity userNotificationEntity = getPostNotificationBuilder(postEntity)
                .notificationType(notificationTypeEntity)
                .sender(null)
                .receiver(postEntity.getOp())
                .build();
        userNotificationRepository.saveAndFlush(userNotificationEntity);
        userNotificationCountDbService.incrementUnreadCount(postEntity.getOp());
        return userNotificationEntity;
    }

    @Override
    public Page<UserNotificationEntity> findUserNotifications(Pageable pageable, UserEntity receiver) {
        return userNotificationRepository.findAllByReceiver(pageable, receiver);
    }

    private UserNotificationEntity.UserNotificationEntityBuilder getPostNotificationBuilder(PostEntity postEntity) {
        return UserNotificationEntity
                .builder()
                .isRead(false)
                .redirectParam(postEntity.getPostId().toString())
                .metaInfo(postEntity.getPostTitle())
                .createdAt(LocalDateTime.now());
    }

}
