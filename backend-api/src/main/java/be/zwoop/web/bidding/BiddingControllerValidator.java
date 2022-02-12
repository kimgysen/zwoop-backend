package be.zwoop.web.bidding;

import be.zwoop.domain.enum_type.BiddingStatusEnum;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.bidding.BiddingRepository;
import be.zwoop.repository.bidding.BiddingStatusEntity;
import be.zwoop.repository.bidding.BiddingStatusRepository;
import be.zwoop.repository.currency.CurrencyEntity;
import be.zwoop.repository.currency.CurrencyRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post.PostRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final BiddingStatusRepository biddingStatusRepository;
    private final CurrencyRepository currencyRepository;


    void validatePrincipal(UUID principalId, UUID respondentId) {
        if (!principalId.equals(respondentId)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Not authorized to submit bidding for this user.");
        }
    }

    UserEntity validateAndGetRespondent(UUID respondentId) {
        Optional<UserEntity> respondentOpt = userRepository.findById(respondentId);
        if (respondentOpt.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Save bidding: Respondent id " + respondentId + " was not found.");
        }
        return respondentOpt.get();
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

    BiddingStatusEntity validateAndGetBiddingStatus(BiddingStatusEnum biddingStatusEnum) {
        Optional<BiddingStatusEntity> biddingStatusOpt = biddingStatusRepository.findByBiddingStatusId(biddingStatusEnum.getValue());
        if (biddingStatusOpt.isEmpty()) {
            throw new ResponseStatusException(CONFLICT, "Save bidding: Bidding id " + BiddingStatusEnum.PENDING.getValue() + " was not found.");
        }
        return biddingStatusOpt.get();
    }

    CurrencyEntity validateAndGetCurrency(String currencyCode) {
        Optional<CurrencyEntity> currencyOpt = currencyRepository.findByCurrencyCode(currencyCode);
        if (currencyOpt.isEmpty()) {
            throw new ResponseStatusException(CONFLICT, "Save bidding: Currency code " + currencyCode + " was not found.");
        }

        return currencyOpt.get();
    }


}