package be.zwoop.service.deal;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;

import java.util.List;
import java.util.UUID;

public interface DealService {
    List<DealEntity> findOpenDealsForUser(UUID userId);
    void saveDeal(PostEntity postEntity, BiddingEntity biddingEntity);
    void removeDealByPost(PostEntity postEntity);

    void sendDealOpenedNotification(PostEntity postEntity, BiddingEntity biddingEntity);
    void sendDealRemovedNotification(PostEntity postEntity, BiddingEntity biddingEntity);

}
