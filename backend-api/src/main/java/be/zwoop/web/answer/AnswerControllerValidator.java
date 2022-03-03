package be.zwoop.web.answer;

import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.answer.AnswerRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post.PostRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Component
@AllArgsConstructor
public class AnswerControllerValidator {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AnswerRepository answerRepository;


    UserEntity validateAndGetPrincipal(UUID principalId) {
        Optional<UserEntity> userOpt = userRepository.findById(principalId);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }
        return userOpt.get();
    }

    PostEntity validateAndGetPost(UUID postId) {
        Optional<PostEntity> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Save bidding: Post id " + postId + " was not found.");
        }
        return postOpt.get();
    }

    AnswerEntity validateAndGetAnswerEntity(UUID answerId) {
        Optional<AnswerEntity> answerEntityOpt = answerRepository.findById(answerId);
        if (answerEntityOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND);
        }
        return answerEntityOpt.get();
    }
}
