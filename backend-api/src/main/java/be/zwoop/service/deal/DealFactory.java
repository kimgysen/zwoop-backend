package be.zwoop.service.deal;


import be.zwoop.amqp.domain.notification.feature.deal.DealOpenedDto;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.deal.DealStatusEntity;
import be.zwoop.repository.post.PostEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class DealFactory {

    public DealEntity buildDealEntity(PostEntity postEntity, BiddingEntity biddingEntity, DealStatusEntity dealStatusEntity) {
        return DealEntity.builder()
                .post(postEntity)
                .dealStatus(dealStatusEntity)
                .asker(postEntity.getAsker())
                .respondent(biddingEntity.getRespondent())
                .dealPrice(biddingEntity.getAskPrice())
                .currency(biddingEntity.getCurrency())
                .build();
    }

    public List<DealOpenedDto> fromDealEntities(List<DealEntity> dealEntities) {
        return dealEntities.stream()
                .map(dealEntity ->
                        DealOpenedDto.builder()
                                .postId(dealEntity.getPost().getPostId())
                                .postTitle(dealEntity.getPost().getPostTitle())
                                .askerId(dealEntity.getAsker().getUserId())
                                .askerNickName(dealEntity.getAsker().getNickName())
                                .respondentId(dealEntity.getRespondent().getUserId())
                                .respondentNickName(dealEntity.getRespondent().getNickName())
                                .dealPrice(dealEntity.getDealPrice())
                                .currencyCode(dealEntity.getCurrency().getCurrencyCode())
                                .build())
                .collect(toList());
    }


}
