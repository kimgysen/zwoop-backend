package be.zwoop.web.bidding;

import be.zwoop.domain.enum_type.BiddingStatusEnum;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.bidding.BiddingRepository;
import be.zwoop.repository.bidding.BiddingStatusEntity;
import be.zwoop.repository.bidding.BiddingStatusRepository;
import be.zwoop.repository.currency.CurrencyRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post.PostRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.web.bidding.dto.BiddingDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/post/{postId}/bidding")
public class BiddingControllerPrivateV1 {

    private final AuthenticationFacade authenticationFacade;
    private final PostRepository postRepository;
    private final BiddingRepository biddingRepository;
    private final BiddingStatusRepository biddingStatusRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;


    @PutMapping("/{respondentId}")
    public ResponseEntity<BiddingEntity> saveBiddingForPost(
            @PathVariable String postId,
            @PathVariable String respondentId,
            BiddingDto biddingDto
    ) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();

        if (!principalId.toString().equals(respondentId)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Not authorized to submit bidding for this user.");
        }

        Optional<PostEntity> postOpt = postRepository.findById(UUID.fromString(postId));
        if (postOpt.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Save bidding: Post id " + postId + " was not found.");
        }

        Optional<UserEntity> respondentOpt = userRepository.findById(UUID.fromString(respondentId));
        if (respondentOpt.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Save bidding: Respondent id " + respondentId + " was not found.");
        }

        Optional<BiddingStatusEntity> biddingStatusOpt = biddingStatusRepository.findByBiddingStatusId(BiddingStatusEnum.PENDING.getValue());
        if (biddingStatusOpt.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Save bidding: Bidding id " + BiddingStatusEnum.PENDING.getValue() + " was not found.");
        }

        Optional<BiddingEntity> biddingEntityOpt = biddingRepository.findByPostEqualsAndRespondentEqualsAndBiddingStatusEquals(
                postOpt.get(), respondentOpt.get(), biddingStatusOpt.get());

        BiddingEntity biddingEntity;
        if (biddingEntityOpt.isEmpty()) {
            biddingEntity = BiddingEntity.builder()
                    .biddingStatus(biddingStatusOpt.get())
                    .post(postOpt.get())
                    .respondent(respondentOpt.get())
                    .askPrice(biddingDto.getAskPrice())
                    .build();
        } else {
            biddingEntity = biddingEntityOpt.get();
            biddingEntity.setAskPrice(biddingDto.getAskPrice());
        }

        biddingRepository.save(biddingEntity);

        return ok(biddingEntity);
    }
}
