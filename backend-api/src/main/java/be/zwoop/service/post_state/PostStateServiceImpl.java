package be.zwoop.service.post_state;

import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.post_status.PostStatusRepository;
import be.zwoop.repository.poststate.PostStateEntity;
import be.zwoop.repository.poststate.PostStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static be.zwoop.domain.enum_type.PostStatusEnum.DEAL_INIT;


@Service
@AllArgsConstructor
public class PostStateServiceImpl implements PostStateService {
    private final PostStateRepository postStateRepository;
    private final PostStatusRepository postStatusRepository;

    @Override
    public Optional<PostStateEntity> findByPost(PostEntity postEntity) {
        return postStateRepository.findByPost(postEntity);
    }

    @Override
    public void setInitDealState(PostEntity postEntity, DealEntity dealEntity) {
        PostStatusEntity dealInit = postStatusRepository.findByPostStatusId(DEAL_INIT.getValue());
        PostStateEntity postState = postEntity.getPostState();

        postState.setPostStatus(dealInit);
        postState.setDeal(dealEntity);

        postStateRepository.saveAndFlush(postState);
    }

    @Override
    public void setAnsweredState(PostEntity post, AnswerEntity answer) {

    }

    @Override
    public void setAnswerAcceptedState(PostEntity post) {

    }

    @Override
    public void setPaidState(PostEntity post) {

    }

    @Override
    public void unsetInitPostState(PostEntity post) {

    }

    @Override
    public void unsetInitDealState(PostEntity post) {

    }

    @Override
    public void unsetAnsweredState(PostEntity postEntity) {

    }
}
