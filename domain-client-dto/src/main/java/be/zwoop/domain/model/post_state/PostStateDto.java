package be.zwoop.domain.model.post_state;

import be.zwoop.domain.model.answer.AnswerDto;
import be.zwoop.domain.model.deal.DealDto;
import be.zwoop.domain.model.post_status.PostStatusDto;
import be.zwoop.repository.poststate.PostStateEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PostStateDto {
    private final UUID postStateId;
    private final PostStatusDto postStatus;
    private final DealDto deal;
    private final AnswerDto answer;

    public static PostStateDto fromPostStateDto(PostStateEntity postStateEntity) {
        DealDto dealDto = postStateEntity.getDeal() != null
                ? DealDto.fromEntity(postStateEntity.getDeal())
                : null;
        AnswerDto answerDto = postStateEntity.getAnswer() != null
                ? AnswerDto.fromEntity(postStateEntity.getAnswer())
                : null;

        return PostStateDto.builder()
                .postStateId(postStateEntity.getPostStateId())
                .postStatus(PostStatusDto.fromEntity(postStateEntity.getPostStatus()))
                .deal(dealDto)
                .answer(answerDto)
                .build();
    }

}
