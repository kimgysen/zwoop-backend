package be.zwoop.repository.bidding;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BiddingStatusRepository extends JpaRepository<BiddingStatusEntity, Integer> {

    Optional<BiddingStatusEntity> findByBiddingStatusId(int biddingStatus);

}
