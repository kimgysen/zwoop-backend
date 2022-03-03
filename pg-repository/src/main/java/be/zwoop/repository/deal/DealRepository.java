package be.zwoop.repository.deal;

import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DealRepository extends JpaRepository<DealEntity, UUID> {

//    @Modifying
//    @Query("delete from DealEntity d where d.dealId = ?1")
//    void deleteByDealId(UUID dealId);

    @Query("""
        select distinct(d) from PostStateEntity ps
        join PostEntity p
        on ps.post = p
        join DealEntity d
        on ps.deal = d
        join BiddingEntity b
        on d.bidding = b
        join PostStatusEntity pst
        on ps.postStatus = pst
        where (
            pst.status = 'DEAL_INIT'
            or pst.status = 'ANSWERED'
        )
        and (
            p.op = :user
            or b.consultant = :user
        )
        order by d.createdAt asc
    """)
    List<DealEntity> findOpenDealsForUser(UserEntity user);

}
