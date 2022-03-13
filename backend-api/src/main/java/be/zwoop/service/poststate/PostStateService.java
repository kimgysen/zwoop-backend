package be.zwoop.service.poststate;

import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.poststate.PostStateEntity;

import java.util.Optional;

public interface PostStateService {

    Optional<PostStateEntity> findByPost(PostEntity postEntity);

    void saveInitPostState(PostEntity postEntity);
    void saveInitDealState(PostEntity postEntity, DealEntity dealEntity);
    void saveAnsweredState(PostEntity postEntity, AnswerEntity answerEntity);
    void saveAnswerAcceptedState(PostEntity postEntity);
    void savePaidState(PostEntity postEntity);

    void rollbackInitDealState(PostEntity postEntity);
    void rollbackAnsweredState(PostEntity postEntity);
}
