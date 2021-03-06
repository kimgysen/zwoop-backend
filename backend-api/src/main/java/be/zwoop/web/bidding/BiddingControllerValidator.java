package be.zwoop.web.bidding;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.bidding.BiddingRepository;
import be.zwoop.repository.currency.CurrencyEntity;
import be.zwoop.repository.currency.CurrencyRepository;
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
public class BiddingControllerValidator {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final BiddingRepository biddingRepository;
    private final CurrencyRepository currencyRepository;


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

    BiddingEntity validateAndGetBiddingEntity(UUID biddingId) {
        Optional<BiddingEntity> biddingEntityOpt = biddingRepository.findById(biddingId);
        if (biddingEntityOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND);
        }
        return biddingEntityOpt.get();
    }

    CurrencyEntity validateAndGetCurrency(String currencyCode) {
        Optional<CurrencyEntity> currencyOpt = currencyRepository.findByCurrencyCode(currencyCode);
        if (currencyOpt.isEmpty()) {
            throw new ResponseStatusException(CONFLICT, "Save bidding: Currency code " + currencyCode + " was not found.");
        }

        return currencyOpt.get();
    }

}
