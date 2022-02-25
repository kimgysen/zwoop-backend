package be.zwoop.web.bidding;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.bidding.BiddingRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post.PostRepository;
import be.zwoop.service.bidding.BiddingService;
import be.zwoop.service.post.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/public/bidding")
public class BiddingControllerPublicV1 {

    private final PostService postService;
    private final BiddingService biddingService;

    @GetMapping
    public List<BiddingEntity> getBiddings (
            @RequestParam UUID postId) {

        Optional<PostEntity> postOpt = postService.findByPostId(postId);

        if (postOpt.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Get biddings: Post id '" + postId + "' was not found.");
        }

        return biddingService.findByPost(postOpt.get());
    }

}
