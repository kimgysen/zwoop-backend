package be.zwoop.service.answer.db;

import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.answer.AnswerRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.service.post_state.PostStateService;
import be.zwoop.web.answer.dto.SaveAnswerDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class AnswerDbServiceImpl implements AnswerDbService {
    private final AnswerRepository answerRepository;
    private final PostStateService postStateService;


    @Override
    public Optional<AnswerEntity> findByAnswerId(UUID answerId) {
        return answerRepository.findById(answerId);
    }

    @Override
    @Transactional
    public AnswerEntity createAnswer(SaveAnswerDto saveAnswerDto, PostEntity postEntity, UserEntity consultantEntity) {
        AnswerEntity answerEntity = AnswerEntity.builder()
                .post(postEntity)
                .answerText(saveAnswerDto.getAnswerText())
                .consultant(consultantEntity)
                .build();

        answerRepository.saveAndFlush(answerEntity);
        postStateService.saveAnsweredState(postEntity, answerEntity);
        return answerEntity;
    }

    @Override
    public void updateAnswer(AnswerEntity toUpdate, SaveAnswerDto updateDto) {
        toUpdate.setAnswerText(updateDto.getAnswerText());
        answerRepository.saveAndFlush(toUpdate);
    }

    @Override
    @Transactional
    public void removeAnswer(AnswerEntity answerEntity) {
        postStateService.rollbackAnsweredState(answerEntity.getPost());
        answerRepository.delete(answerEntity);
    }

}
