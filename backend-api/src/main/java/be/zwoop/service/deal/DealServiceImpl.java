package be.zwoop.service.deal;

import be.zwoop.amqp.domain.notification.NotificationDto;
import be.zwoop.amqp.domain.notification.feature.deal.DealCancelledDto;
import be.zwoop.amqp.domain.notification.feature.deal.DealOpenedDto;
import be.zwoop.amqp.notification.NotificationSender;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.deal.DealRepository;
import be.zwoop.repository.deal.DealStatusEntity;
import be.zwoop.repository.deal.DealStatusRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static be.zwoop.amqp.domain.notification.NotificationType.DEAL_CANCELLED;
import static be.zwoop.amqp.domain.notification.NotificationType.DEAL_OPENED;
import static be.zwoop.domain.enum_type.DealStatusEnum.OPEN;

@Service
@AllArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealFactory dealFactory;
    private final DealRepository dealRepository;
    private final DealStatusRepository dealStatusRepository;
    private final UserRepository userRepository;
    private final NotificationSender notificationSender;


    @Override
    public List<DealEntity> findOpenDealsForUser(UUID userId) {
        DealStatusEntity dealStatus = dealStatusRepository.findByDealStatusId(OPEN.getValue());

        Optional<UserEntity> userOpt = userRepository.findByUserIdAndBlockedAndActive(userId, false, true);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found.");
        }

        return dealRepository.findOpenDealsForUser(dealStatus, userOpt.get());
    }

    @Override
    public void saveDeal(PostEntity postEntity, BiddingEntity biddingEntity) {
        DealStatusEntity dealStatusEntity = dealStatusRepository.findByDealStatusId(OPEN.getValue());
        DealEntity dealEntity = dealFactory.buildDealEntity(postEntity, biddingEntity, dealStatusEntity);
        dealRepository.saveAndFlush(dealEntity);
    }

    @Override
    public void removeDealByPost(PostEntity postEntity) {
        Optional<DealEntity> dealOpt = dealRepository.findByPost(postEntity);
        dealOpt.ifPresent(dealRepository::delete);
    }

    @Override
    public void sendDealOpenedNotification(PostEntity postEntity, BiddingEntity biddingEntity) {
        notificationSender.sendNotification(
                NotificationDto.builder()
                    .notificationType(DEAL_OPENED)
                    .dto(
                            DealOpenedDto.builder()
                                    .postId(postEntity.getPostId())
                                    .postTitle(postEntity.getPostTitle())
                                    .askerId(postEntity.getAsker().getUserId())
                                    .askerNickName(postEntity.getAsker().getNickName())
                                    .respondentId(biddingEntity.getRespondent().getUserId())
                                    .respondentNickName(biddingEntity.getRespondent().getNickName())
                                    .dealPrice(biddingEntity.getAskPrice())
                                    .currencyCode(biddingEntity.getCurrency().getCurrencyCode())
                                    .build())
                    .build());
    }

    @Override
    public void sendDealRemovedNotification(PostEntity postEntity, BiddingEntity biddingEntity) {
        notificationSender.sendNotification(
                NotificationDto.builder()
                        .notificationType(DEAL_CANCELLED)
                        .dto(
                                DealCancelledDto
                                        .builder()
                                        .postId(postEntity.getPostId())
                                        .postTitle(postEntity.getPostTitle())
                                        .askerId(postEntity.getAsker().getUserId())
                                        .respondentId(biddingEntity.getRespondent().getUserId())
                                        .build())
                        .build());
    }

}

