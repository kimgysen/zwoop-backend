package be.zwoop.repository.deal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DealStatusRepository extends JpaRepository<DealStatusEntity, UUID> {

    DealStatusEntity findByDealStatusId(int dealStatus);

}
