package be.zwoop.web.bidding;

import be.zwoop.domain.enum_type.BiddingStatusEnum;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.bidding.BiddingRepository;
import be.zwoop.repository.bidding.BiddingStatusEntity;
import be.zwoop.repository.currency.CurrencyEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.service.bidding.BiddingService;
import be.zwoop.web.bidding.dto.BiddingDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/post/{postId}/bidding")
public class BiddingControllerPrivateV1 {

    private final AuthenticationFacade authenticationFacade;
    private final BiddingRepository biddingRepository;
    private final BiddingControllerValidator validator;
    private final BiddingService biddingService;


    @PutMapping("/{biddingId}/accept")
    public ResponseEntity<Void> acceptBidding(
            @PathVariable UUID postId,
            @PathVariable UUID biddingId) {

        UUID principalId = authenticationFacade.getAuthenticatedUserId();

        PostEntity postEntity = validator.validateAndGetPost(postId);
        BiddingStatusEntity pendingStatusEntity = validator.validateAndGetBiddingStatus(BiddingStatusEnum.PENDING);

        Optional<BiddingEntity> pendingBiddingEntityOpt = biddingService.findByPostAndBiddingStatus(
                postEntity, pendingStatusEntity);

        if (pendingBiddingEntityOpt.isPresent()) {
            throw new ResponseStatusException(CONFLICT, "post already has an accepted bidding");

        } else {
            BiddingEntity biddingEntity = validator.validateAndGetBiddingEntity(biddingId);
            validator.validatePrincipal(principalId, biddingEntity.getRespondent().getUserId());

            BiddingStatusEntity acceptedStatusEntity = validator.validateAndGetBiddingStatus(BiddingStatusEnum.ACCEPTED);
            biddingEntity.setBiddingStatus(acceptedStatusEntity);

            biddingService.saveBidding(biddingEntity);
            biddingService.sendBiddingAcceptedToQueue(biddingEntity);

            return noContent().build();
        }
    }

    // TODO: Analyse how to do this
    // When the smart contract was closed, this is not as simple as just removing the acceptance
    @DeleteMapping("/{biddingId}/accept")
    public ResponseEntity<Void> removeAcceptBidding(@PathVariable UUID biddingId) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();

        BiddingEntity biddingEntity = validator.validateAndGetBiddingEntity(biddingId);
        validator.validatePrincipal(principalId, biddingEntity.getRespondent().getUserId());

        BiddingStatusEntity pendingStatusEntity = validator.validateAndGetBiddingStatus(BiddingStatusEnum.PENDING);
        biddingEntity.setBiddingStatus(pendingStatusEntity);

        biddingService.saveBidding(biddingEntity);

        return noContent().build();
    }

    @PutMapping("/respondent/{respondentId}")
    public ResponseEntity<Void> saveBiddingForPost(
            @PathVariable UUID postId,
            @PathVariable UUID respondentId,
            @Valid @RequestBody BiddingDto biddingDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();

        validator.validatePrincipal(principalId, respondentId);

        UserEntity respondentEntity = validator.validateAndGetRespondent(respondentId);
        PostEntity postEntity = validator.validateAndGetPost(postId);
        BiddingStatusEntity biddingStatusEntity = validator.validateAndGetBiddingStatus(BiddingStatusEnum.PENDING);
        CurrencyEntity currencyEntity = validator.validateAndGetCurrency(biddingDto.getCurrencyCode());

        Optional<BiddingEntity> biddingEntityOpt = biddingService.findByPostAndRespondentAndBiddingStatus(
                postEntity, respondentEntity, biddingStatusEntity);

        if (biddingEntityOpt.isEmpty()) {
            BiddingEntity biddingEntity = BiddingEntity.builder()
                    .biddingStatus(biddingStatusEntity)
                    .post(postEntity)
                    .respondent(respondentEntity)
                    .askPrice(biddingDto.getAskPrice())
                    .currency(currencyEntity)
                    .build();
            biddingService.saveBidding(biddingEntity);
            biddingService.sendBiddingAddedToQueue(biddingEntity);

        } else {
            BiddingEntity biddingEntity = biddingEntityOpt.get();

            if (!biddingDto.getAskPrice().equals(biddingEntity.getAskPrice())) {
                biddingEntity.setAskPrice(biddingDto.getAskPrice());
                biddingService.saveBidding(biddingEntity);
                biddingService.sendBiddingChangedToQueue(biddingEntity);
            }

        }

        return noContent().build();
    }

    @DeleteMapping("/respondent/{respondentId}")
    public ResponseEntity<Void> deleteBiddingForPost(
            @PathVariable UUID postId,
            @PathVariable UUID respondentId) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();

        validator.validatePrincipal(principalId, respondentId);
        UserEntity respondentEntity = validator.validateAndGetRespondent(respondentId);
        PostEntity postEntity = validator.validateAndGetPost(postId);

        Optional<BiddingEntity> biddingEntityOpt = biddingService.findByPostAndRespondent(postEntity, respondentEntity);

        if (biddingEntityOpt.isPresent()) {
            BiddingEntity biddingEntity = biddingEntityOpt.get();
            biddingService.removeBidding(biddingEntity);
            biddingService.sendBiddingRemovedToQueue(biddingEntity);

        } else {
            throw new ResponseStatusException(NOT_FOUND);
        }

        return noContent().build();
    }



}
