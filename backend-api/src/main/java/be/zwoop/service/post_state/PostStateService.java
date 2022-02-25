package be.zwoop.service.post_state;

import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.poststate.PostStateEntity;

import java.util.Optional;

public interface PostStateService {

    Optional<PostStateEntity> findByPost(PostEntity postEntity);

    void setInitDealState(PostEntity post, DealEntity dealEntity);
    void setAnsweredState(PostEntity post, AnswerEntity answer);
    void setAnswerAcceptedState(PostEntity post);
    void setPaidState(PostEntity post);

    void unsetInitPostState(PostEntity post);
    void unsetInitDealState(PostEntity post);
    void unsetAnsweredState(PostEntity postEntity);
}
