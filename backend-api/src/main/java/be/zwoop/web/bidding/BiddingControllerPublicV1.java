package be.zwoop.web.bidding;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.bidding.BiddingRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/public/post/{postId}/bidding")
public class BiddingControllerPublicV1 {

    private final PostRepository postRepository;
    private final BiddingRepository biddingRepository;

    @GetMapping
    public List<BiddingEntity> getBiddingForPost(
            @PathVariable String postId) {

        Optional<PostEntity> postOpt = postRepository.findById(UUID.fromString(postId));
        if (postOpt.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Save bidding: Post id " + postId + " was not found.");
        }

        return biddingRepository.findAllByPostEquals(postOpt.get());
    }

}
