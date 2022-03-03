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

import static be.zwoop.domain.enum_type.PostStatusEnum.*;


@Service
@AllArgsConstructor
public class PostStateServiceImpl implements PostStateService {
    private final PostStateRepository postStateRepository;
    private final PostStatusRepository postStatusRepository;
    private final PostStateFactory postStateFactory;

    @Override
    public Optional<PostStateEntity> findByPost(PostEntity postEntity) {
        return postStateRepository.findByPost(postEntity);
    }

    @Override
    public void saveInitPostState(PostEntity postEntity) {
        PostStateEntity postStateEntity = postStateFactory.buildPostState(postEntity, POST_INIT);
        postStateRepository.saveAndFlush(postStateEntity);
    }

    @Override
    public void saveInitDealState(PostEntity postEntity, DealEntity dealEntity) {
        PostStatusEntity dealInit = postStatusRepository.findByPostStatusId(DEAL_INIT.getValue());
        PostStateEntity postState = postEntity.getPostState();

        postState.setPostStatus(dealInit);
        postState.setDeal(dealEntity);

        postStateRepository.saveAndFlush(postState);
    }

    @Override
    public void saveAnsweredState(PostEntity postEntity, AnswerEntity answerEntity) {
        PostStatusEntity answered = postStatusRepository.findByPostStatusId(ANSWERED.getValue());
        PostStateEntity postState = postEntity.getPostState();

        postState.setPostStatus(answered);
        postState.setAnswer(answerEntity);

        postStateRepository.saveAndFlush(postState);
    }

    @Override
    public void saveAnswerAcceptedState(PostEntity postEntity) {

    }

    @Override
    public void savePaidState(PostEntity post) {

    }

    @Override
    public void rollbackInitDealState(PostEntity postEntity) {
        PostStatusEntity postInit = postStatusRepository.findByPostStatusId(POST_INIT.getValue());
        PostStateEntity postState = postEntity.getPostState();

        postState.setPostStatus(postInit);
        postState.setDeal(null);

        postStateRepository.saveAndFlush(postState);

    }

    @Override
    public void rollbackAnsweredState(PostEntity postEntity) {
        PostStatusEntity postInit = postStatusRepository.findByPostStatusId(DEAL_INIT.getValue());
        PostStateEntity postState = postEntity.getPostState();

        postState.setPostStatus(postInit);
        postState.setAnswer(null);

        postStateRepository.saveAndFlush(postState);
    }
}
