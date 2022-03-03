package be.zwoop.service.deal.db;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;

import java.util.List;
import java.util.UUID;

public interface DealDbService {
    List<DealEntity> findOpenDealsForUser(UUID userId);
    DealEntity saveDeal(BiddingEntity biddingEntity);
    void removeDeal(DealEntity deal);

}
