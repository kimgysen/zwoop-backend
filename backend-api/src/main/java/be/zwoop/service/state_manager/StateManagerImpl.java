package be.zwoop.service.state_manager;


import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.service.bidding.BiddingService;
import be.zwoop.service.deal.DealService;
import be.zwoop.service.post.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static be.zwoop.domain.enum_type.BiddingStatusEnum.ACCEPTED;
import static be.zwoop.domain.enum_type.BiddingStatusEnum.PENDING;
import static be.zwoop.domain.enum_type.PostStatusEnum.IN_PROGRESS;
import static be.zwoop.domain.enum_type.PostStatusEnum.OPEN;

@AllArgsConstructor
@Service
public class StateManagerImpl implements StateManager {

    private final PostService postService;
    private final BiddingService biddingService;
    private final DealService dealService;


    @Override
    public boolean canPostBeUpdated(PostEntity postEntity) {
        return postEntity.getPostStatus().getPostStatus()
                .equals(PostStatusEnum.OPEN.name());
    }

    @Override
    @Transactional
    public void acceptBidding(PostEntity postEntity, BiddingEntity biddingEntity) {
        postService.updatePostStatus(postEntity, IN_PROGRESS);
        biddingService.updateBiddingStatus(biddingEntity, ACCEPTED);
        dealService.saveDeal(postEntity, biddingEntity);
    }

    @Override
    @Transactional
    public void removeAcceptBidding(PostEntity postEntity, BiddingEntity biddingEntity) {
        postService.updatePostStatus(postEntity, OPEN);
        biddingService.updateBiddingStatus(biddingEntity, PENDING);
        dealService.removeDealByPost(postEntity);
    }

    @Override
    @Transactional
    public void answerPost(PostEntity postEntity) {

    }

    @Override
    @Transactional
    public void removeAnswerPost(PostEntity postEntity) {

    }
}
