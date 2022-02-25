package be.zwoop.service.deal;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;

import java.util.List;
import java.util.UUID;

public interface DealService {
    List<DealEntity> findOpenDealsForUser(UUID userId);
    DealEntity saveDeal(BiddingEntity biddingEntity);
    void removeDeal(DealEntity deal);
    //void removeDeal(UUID dealId);

    void sendDealInitNotification(DealEntity dealEntity);
    void sendDealRemovedNotification(DealEntity dealEntity);

}
