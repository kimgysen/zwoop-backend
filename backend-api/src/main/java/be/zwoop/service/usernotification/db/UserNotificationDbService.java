package be.zwoop.service.usernotification.db;

import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.usernotification.UserNotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserNotificationDbService {
    UserNotificationEntity saveBiddingAddedNotification(BiddingEntity biddingEntity);
    UserNotificationEntity saveBiddingChangedNotification(BiddingEntity biddingEntity);
    UserNotificationEntity saveBiddingRemoveNotification(BiddingEntity biddingEntity);
    UserNotificationEntity saveDealInitNotification(DealEntity dealEntity);
    UserNotificationEntity saveDealCancelledNotification(DealEntity dealEntity);
    UserNotificationEntity saveAnswerAddedNotification(AnswerEntity answerEntity);
    UserNotificationEntity saveAnswerChangedNotification(AnswerEntity answerEntity);
    UserNotificationEntity saveAnswerRemovedNotification(AnswerEntity answerEntity);
    UserNotificationEntity saveAnswerAcceptedNotification(AnswerEntity answerEntity);
    UserNotificationEntity saveAnswerPaidNotification(AnswerEntity answerEntity);

    Page<UserNotificationEntity> findUserNotifications(Pageable pageable, UserEntity receiver);
}
