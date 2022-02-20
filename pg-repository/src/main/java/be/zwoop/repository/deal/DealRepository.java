package be.zwoop.repository.deal;

import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DealRepository extends JpaRepository<DealEntity, UUID> {

    Optional<DealEntity> findByPost(PostEntity postEntity);

    @Modifying
    @Query("delete from DealEntity d where d.dealId = ?1")
    void deleteByDealId(UUID dealId);

    @Query("""
        select distinct(d) from DealEntity d 
        where d.dealStatus = :dealStatus
        and (
            d.asker = :user
            or d.respondent = :user
        )
        order by d.createdAt asc
    """)
    List<DealEntity> findOpenDealsForUser(DealStatusEntity dealStatus, UserEntity user);

}
