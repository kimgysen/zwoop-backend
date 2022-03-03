package be.zwoop.service.answer;


import be.zwoop.amqp.domain.model.UserDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerAddedDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerChangedDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerRemovedDto;
import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.web.answer.dto.SaveAnswerDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AnswerFactory {

    public AnswerEntity buildAnswerFromDto(SaveAnswerDto saveAnswerDto, PostEntity postEntity, UserEntity consultantEntity) {
        return AnswerEntity.builder()
                .post(postEntity)
                .answerText(saveAnswerDto.getAnswerText())
                .consultant(consultantEntity)
                .build();
    }

    public AnswerAddedDto buildAnswerAddedDto(AnswerEntity answerEntity) {
        UserEntity op = answerEntity.getPost().getOp();
        UserEntity consultantEntity = answerEntity.getConsultant();
        return AnswerAddedDto.builder()
                .answerId(answerEntity.getAnswerId())
                .answerText(answerEntity.getAnswerText())
                .op(UserDto.builder()
                        .userId(op.getUserId())
                        .nickName(op.getNickName())
                        .avatar(op.getProfilePic())
                        .build())
                .consultant(UserDto.builder()
                        .userId(consultantEntity.getUserId())
                        .nickName(consultantEntity.getNickName())
                        .avatar(consultantEntity.getProfilePic())
                        .build())
                .createdAt(answerEntity.getCreatedAt())
                .updatedAt(answerEntity.getUpdatedAt())
                .build();
    }

    public AnswerChangedDto buildAnswerChangedDto(AnswerEntity answerEntity) {
        UserEntity op = answerEntity.getPost().getOp();
        UserEntity consultantEntity = answerEntity.getConsultant();

        return AnswerChangedDto.builder()
                .answerId(answerEntity.getAnswerId())
                .answerText(answerEntity.getAnswerText())
                .op(UserDto.builder()
                        .userId(op.getUserId())
                        .nickName(op.getNickName())
                        .avatar(op.getProfilePic())
                        .build())
                .consultant(UserDto.builder()
                        .userId(consultantEntity.getUserId())
                        .nickName(consultantEntity.getNickName())
                        .avatar(consultantEntity.getProfilePic())
                        .build())
                .createdAt(answerEntity.getCreatedAt())
                .updatedAt(answerEntity.getUpdatedAt())
                .build();
    }

    public AnswerRemovedDto buildAnswerRemovedDto(AnswerEntity answerEntity) {
        UserEntity op = answerEntity.getPost().getOp();
        UserEntity consultantEntity = answerEntity.getConsultant();
        return AnswerRemovedDto.builder()
                .answerId(answerEntity.getAnswerId())
                .op(UserDto.builder()
                        .userId(op.getUserId())
                        .nickName(op.getNickName())
                        .avatar(op.getProfilePic())
                        .build())
                .consultant(UserDto.builder()
                        .userId(consultantEntity.getUserId())
                        .nickName(consultantEntity.getNickName())
                        .avatar(consultantEntity.getProfilePic())
                        .build())
                .build();
    }

}
