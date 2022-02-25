package be.zwoop.service.deal;

import be.zwoop.amqp.domain.common.feature.deal.DealCancelledDto;
import be.zwoop.amqp.domain.common.feature.deal.DealInitDto;
import be.zwoop.amqp.domain.model.UserDto;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class DealFactory {

    public DealEntity buildDealEntity(BiddingEntity biddingEntity) {
        return DealEntity.builder()
                .bidding(biddingEntity)
                .build();
    }

    public DealInitDto buildDealInitDto(DealEntity dealEntity) {
        PostEntity postEntity = dealEntity.getBidding().getPost();
        UserEntity opEntity = postEntity.getOp();
        UserEntity consultantEntity = dealEntity.getBidding().getConsultant();

        return DealInitDto.builder()
                .postId(postEntity.getPostId())
                .postTitle(postEntity.getPostTitle())
                .op(UserDto.builder()
                        .userId(opEntity.getUserId())
                        .nickName(opEntity.getNickName())
                        .avatar(opEntity.getProfilePic())
                        .build())
                .consultant(UserDto.builder()
                        .userId(consultantEntity.getUserId())
                        .nickName(consultantEntity.getNickName())
                        .avatar(consultantEntity.getProfilePic())
                        .build())
                .dealPrice(dealEntity.getBidding().getAskPrice())
                .currencyCode(dealEntity.getBidding().getCurrency().getCurrencyCode())
                .build();
    }

    public DealCancelledDto buildDealCancelledDto(DealEntity dealEntity) {
        PostEntity postEntity = dealEntity.getBidding().getPost();
        UserEntity opEntity = postEntity.getOp();
        UserEntity consultantEntity = dealEntity.getBidding().getConsultant();

        return DealCancelledDto.builder()
                .dealId(dealEntity.getDealId())
                .op(UserDto.builder()
                        .userId(opEntity.getUserId())
                        .nickName(opEntity.getNickName())
                        .avatar(opEntity.getProfilePic())
                        .build())
                .consultant(UserDto.builder()
                        .userId(consultantEntity.getUserId())
                        .nickName(consultantEntity.getNickName())
                        .avatar(consultantEntity.getProfilePic())
                        .build())
                .build();
    }

    public List<DealInitDto> buildDealInitDtos(List<DealEntity> dealEntities) {
        return dealEntities.stream()
                .map(this::buildDealInitDto)
                .collect(toList());
    }

}
