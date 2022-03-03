package be.zwoop.service.deal.notification;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;

import java.util.List;
import java.util.UUID;

public interface DealNotificationService {
    void sendDealInitNotification(DealEntity dealEntity);
    void sendDealRemovedNotification(DealEntity dealEntity);

}
