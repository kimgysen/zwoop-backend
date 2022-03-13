package be.zwoop.web.deal;


import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.bidding.BiddingRepository;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.deal.DealRepository;
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
public class DealControllerValidator {

    private final UserRepository userRepository;
    private final DealRepository dealRepository;
    private final BiddingRepository biddingRepository;

    UserEntity validateAndGetPrincipal(UUID principalId) {
        Optional<UserEntity> userOpt = userRepository.findById(principalId);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }
        return userOpt.get();
    }

    BiddingEntity validateAndGetBiddingEntity(UUID biddingId) {
        Optional<BiddingEntity> biddingEntityOpt = biddingRepository.findById(biddingId);
        if (biddingEntityOpt.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        return biddingEntityOpt.get();
    }

    DealEntity validateAndGetDealEntity(UUID dealId) {
        Optional<DealEntity> dealEntityOpt = dealRepository.findById(dealId);
        if (dealEntityOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND);
        }
        return dealEntityOpt.get();
    }

}
