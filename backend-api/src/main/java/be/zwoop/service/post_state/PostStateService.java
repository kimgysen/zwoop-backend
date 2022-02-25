package be.zwoop.service.post_state;

import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.poststate.PostStateEntity;

import java.util.Optional;

public interface PostStateService {

    Optional<PostStateEntity> findByPost(PostEntity postEntity);

    void setInitDealState(PostEntity postEntity, DealEntity dealEntity);
    void setAnsweredState(PostEntity postEntity, AnswerEntity answerEntity);
    void setAnswerAcceptedState(PostEntity postEntity);
    void setPaidState(PostEntity postEntity);

    void unsetInitDealState(PostEntity postEntity);
    void unsetAnsweredState(PostEntity postEntity);
}
