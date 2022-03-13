package be.zwoop.web.answer;

import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.domain.model.answer.AnswerDto;
import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.poststate.PostStateEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.service.answer.db.AnswerDbService;
import be.zwoop.service.answer.notification.AnswerNotificationService;
import be.zwoop.web.answer.dto.SaveAnswerDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/answer")
public class AnswerControllerPrivateV1 {
    private final AuthenticationFacade authenticationFacade;
    private final AnswerControllerValidator validator;
    private final AnswerDbService answerDbService;
    private final AnswerNotificationService answerNotificationService;

    @PostMapping
    public ResponseEntity<AnswerDto> createAnswer(@Valid @RequestBody SaveAnswerDto saveAnswerDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();

        UserEntity principal = validator.validateAndGetPrincipal(principalId);
        PostEntity postEntity = validator.validateAndGetPost(saveAnswerDto.getPostId());
        PostStateEntity postStateEntity = postEntity.getPostState();
        DealEntity dealEntity = postStateEntity.getDeal();
        UserEntity consultantEntity = dealEntity.getBidding().getConsultant();

        if (!Objects.equals(dealEntity.getBidding().getConsultant(), principal)) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        if (!Objects.equals(
                postStateEntity.getPostStatus().getStatus(), PostStatusEnum.DEAL_INIT.name())) {
            throw new ResponseStatusException(BAD_REQUEST, "Create answer: post is not in DEAL_INIT state");
        }

        if (postStateEntity.getDeal() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Create answer: post doesn'\t have a deal");
        }

        AnswerEntity answerEntity = answerDbService.createAnswer(saveAnswerDto, postEntity, consultantEntity);
        answerNotificationService.sendAnswerAddedNotification(answerEntity);

        URI uri = UriComponentsBuilder
                .fromPath(("/answer/{id}"))
                .buildAndExpand(answerEntity.getAnswerId()).toUri();

        return created(uri).body(
                AnswerDto.fromEntity(answerEntity));
    }

    @PutMapping("/{answerId}")
    public ResponseEntity<AnswerDto> updateAnswer(
            @PathVariable UUID answerId,
            @Valid @RequestBody SaveAnswerDto saveAnswerDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetPrincipal(principalId);
        AnswerEntity answerEntity = validator.validateAndGetAnswerEntity(answerId);
        PostEntity postEntity = answerEntity.getPost();
        PostStateEntity postStateEntity = postEntity.getPostState();

        if (!Objects.equals(answerEntity.getConsultant(), principal)) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        if (!Objects.equals(
                postStateEntity.getPostStatus().getStatus(), PostStatusEnum.ANSWERED.name())) {
            throw new ResponseStatusException(BAD_REQUEST, "Update answer: post is not in ANSWERED state");
        }

        answerDbService.updateAnswer(answerEntity, saveAnswerDto);
        answerNotificationService.sendAnswerChangedNotification(answerEntity);

        return ok(AnswerDto.fromEntity(answerEntity));
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable UUID answerId) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetPrincipal(principalId);
        AnswerEntity answerEntity = validator.validateAndGetAnswerEntity(answerId);
        PostEntity postEntity = answerEntity.getPost();
        PostStateEntity postStateEntity = postEntity.getPostState();

        if (!Objects.equals(answerEntity.getConsultant(), principal)) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        if (!Objects.equals(
                postStateEntity.getPostStatus().getStatus(), PostStatusEnum.ANSWERED.name())) {
            throw new ResponseStatusException(BAD_REQUEST, "Update answer: post is not in ANSWERED state");
        }

        answerDbService.removeAnswer(answerEntity);
        answerNotificationService.sendAnswerRemovedNotification(answerEntity);

        return noContent().build();
    }

    @PutMapping("/{answerId}/accept")
    public ResponseEntity<Void> acceptAnswer(@PathVariable UUID answerId) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetPrincipal(principalId);

        AnswerEntity answerEntity = validator.validateAndGetAnswerEntity(answerId);
        PostEntity postEntity = answerEntity.getPost();

        if (!Objects.equals(postEntity.getOp(), principal)) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        answerDbService.acceptAnswer(answerEntity);

        return noContent().build();
    }


}
