package be.zwoop.web.answer;


import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.service.answer.db.AnswerDbService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/public/answer")
public class AnswerControllerPublicV1 {

    private final AnswerDbService answerDbService;

    @GetMapping("/{answerId}")
    public ResponseEntity<AnswerEntity> getAnswer(@PathVariable UUID answerId) {
        Optional<AnswerEntity> answerEntity = answerDbService.findByAnswerId(answerId);

        if (answerEntity.isPresent()) {
            return ok(answerEntity.get());

        } else {
            throw new ResponseStatusException(NOT_FOUND);

        }
    }

}
