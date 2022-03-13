package be.zwoop.service.answer.db;

import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.web.answer.dto.SaveAnswerDto;

import java.util.Optional;
import java.util.UUID;

public interface AnswerDbService {
    Optional<AnswerEntity> findByAnswerId(UUID answerId);
    AnswerEntity createAnswer(SaveAnswerDto saveAnswerDto, PostEntity postEntity, UserEntity consultantEntity);
    void updateAnswer(AnswerEntity toUpdate, SaveAnswerDto updateDto);
    void removeAnswer(AnswerEntity answerEntity);
    void acceptAnswer(AnswerEntity answerEntity);
}
