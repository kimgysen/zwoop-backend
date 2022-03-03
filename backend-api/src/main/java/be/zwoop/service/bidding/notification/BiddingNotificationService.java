package be.zwoop.service.bidding.notification;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BiddingNotificationService {
    void sendBiddingAddedNotification(BiddingEntity biddingEntity);
    void sendBiddingChangedNotification(BiddingEntity biddingEntity);
    void sendBiddingRemovedNotification(BiddingEntity biddingEntity);

}
