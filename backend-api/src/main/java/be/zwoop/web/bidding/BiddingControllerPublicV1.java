package be.zwoop.web.bidding;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.service.bidding.db.BiddingDbService;
import be.zwoop.service.post.db.PostDbService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/public/bidding")
public class BiddingControllerPublicV1 {

    private final PostDbService postDbService;
    private final BiddingDbService biddingDbService;

    @GetMapping
    public List<BiddingEntity> getBiddings (
            @RequestParam UUID postId) {

        Optional<PostEntity> postOpt = postDbService.findByPostId(postId);

        if (postOpt.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Get biddings: Post id '" + postId + "' was not found.");
        }

        return biddingDbService.findByPost(postOpt.get());
    }

}
