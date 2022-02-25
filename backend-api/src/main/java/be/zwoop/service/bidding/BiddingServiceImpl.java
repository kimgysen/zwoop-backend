package be.zwoop.service.bidding;

import be.zwoop.amqp.domain.post_update.PostUpdateDto;
import be.zwoop.amqp.domain.post_update.PostUpdateType;
import be.zwoop.amqp.post.PostNotificationSender;
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
public class BiddingServiceImpl implements BiddingService{

    private final BiddingFactory biddingFactory;
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

    @Override
    public void sendBiddingAddedNotification(BiddingEntity biddingEntity) {
        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_ADDED)
                        .dto(biddingFactory
                                .buildBiddingAddedDto(biddingEntity))
                        .build());
    }

    @Override
    public void sendBiddingChangedNotification(BiddingEntity biddingEntity) {
        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_CHANGED)
                        .dto(biddingFactory
                                .buildBiddingChangedDto(biddingEntity))
                        .build());
    }

    @Override
    public void sendBiddingRemovedNotification(BiddingEntity biddingEntity) {
        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_REMOVED)
                        .dto(biddingFactory
                                .buildBiddingRemovedDto(biddingEntity))
                        .build());
    }

}
