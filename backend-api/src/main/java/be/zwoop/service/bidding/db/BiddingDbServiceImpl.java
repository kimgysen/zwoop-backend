package be.zwoop.service.bidding.db;

import be.zwoop.amqp.topic.post_notification.PostNotificationSender;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.bidding.BiddingRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class BiddingDbServiceImpl implements BiddingDbService {

    private final BiddingRepository biddingRepository;
    private final PostNotificationSender postNotificationSender;

    @Override
    public Optional<BiddingEntity> findByBiddingId(UUID biddingId) {
        return biddingRepository.findById(biddingId);
    }

    @Override
    public List<BiddingEntity> findByPost(PostEntity postEntity) {
        return biddingRepository.findByPostEquals(postEntity);
    }

    @Override
    public Optional<BiddingEntity> findByPostAndConsultant(PostEntity postEntity, UserEntity consultantEntity) {
        return biddingRepository.findByPostEqualsAndConsultantEquals(postEntity, consultantEntity);
    }

    @Override
    public BiddingEntity saveBidding(BiddingEntity biddingEntity) {
        return biddingRepository.saveAndFlush(biddingEntity);
    }

    @Override
    public void removeBidding(BiddingEntity biddingEntity) {
        biddingRepository.delete(biddingEntity);
    }

}
