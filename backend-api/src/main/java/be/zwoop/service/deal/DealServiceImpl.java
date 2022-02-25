package be.zwoop.service.deal;

import be.zwoop.amqp.domain.common.feature.deal.DealCancelledDto;
import be.zwoop.amqp.domain.common.feature.deal.DealInitDto;
import be.zwoop.amqp.domain.model.UserDto;
import be.zwoop.amqp.domain.notification.NotificationDto;
import be.zwoop.amqp.domain.post.PostUpdateDto;
import be.zwoop.amqp.domain.post.PostUpdateType;
import be.zwoop.amqp.notification.NotificationSender;
import be.zwoop.amqp.post.PostNotificationSender;
import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.deal.DealRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.post_status.PostStatusRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.service.post_state.PostStateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static be.zwoop.amqp.domain.notification.NotificationType.DEAL_CANCELLED;
import static be.zwoop.amqp.domain.notification.NotificationType.DEAL_INIT;

@Service
@AllArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealFactory dealFactory;
    private final DealRepository dealRepository;
    private final PostStatusRepository postStatusRepository;
    private final UserRepository userRepository;
    private final PostStateService postStateService;
    private final NotificationSender notificationSender;
    private final PostNotificationSender postNotificationSender;


    @Override
    public List<DealEntity> findOpenDealsForUser(UUID userId) {
        PostStatusEntity dealInitStatus = postStatusRepository.findByPostStatusId(PostStatusEnum.DEAL_INIT.getValue());

        Optional<UserEntity> userOpt = userRepository.findByUserIdAndBlockedAndActive(userId, false, true);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found.");
        }

        return dealRepository.findOpenDealsForUser(dealInitStatus, userOpt.get());
    }

    @Override
    @Transactional
    public DealEntity saveDeal(BiddingEntity biddingEntity) {
        DealEntity dealEntity = dealFactory.buildDealEntity(biddingEntity);
        DealEntity saved = dealRepository.saveAndFlush(dealEntity);
        postStateService.setInitDealState(biddingEntity.getPost(), dealEntity);
        return saved;
    }

    public void removeDeal(DealEntity deal) {
        dealRepository.delete(deal);
    }

//    @Transactional
//    @Override
//    public void removeDeal(UUID dealId) {
//        dealRepository.deleteByDealId(dealId);
//    }

    @Override
    public void sendDealInitNotification(DealEntity dealEntity) {
        BiddingEntity biddingEntity = dealEntity.getBidding();
        PostEntity postEntity = biddingEntity.getPost();

        notificationSender.sendNotification(
                NotificationDto.builder()
                    .notificationType(DEAL_INIT)
                    .dto(dealEntity)
                    .build());

        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.DEAL_INIT)
                        .dto(dealFactory
                                .buildDealInitDto(dealEntity))
                        .build());
    }

    @Override
    public void sendDealRemovedNotification(DealEntity dealEntity) {
        BiddingEntity biddingEntity = dealEntity.getBidding();
        PostEntity postEntity = biddingEntity.getPost();

        notificationSender.sendNotification(
                NotificationDto.builder()
                        .notificationType(DEAL_CANCELLED)
                        .dto(dealFactory.
                                buildDealCancelledDto(dealEntity))
                        .build());

        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.DEAL_INIT)
                        .dto(dealEntity)
                        .build());
    }

}

