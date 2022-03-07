package be.zwoop.web.bidding;

import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.domain.model.bidding.BiddingDto;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.currency.CurrencyEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.poststate.PostStateEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.service.bidding.db.BiddingDbService;
import be.zwoop.service.bidding.notification.BiddingNotificationService;
import be.zwoop.web.bidding.dto.CreateBiddingDto;
import be.zwoop.web.bidding.dto.UpdateBiddingDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/bidding")
public class BiddingControllerPrivateV1 {

    private final AuthenticationFacade authenticationFacade;
    private final BiddingControllerValidator validator;
    private final BiddingNotificationService biddingNotificationService;
    private final BiddingDbService biddingDbService;


    @PostMapping
    public ResponseEntity<BiddingDto> createBidding(@Valid @RequestBody CreateBiddingDto createBiddingDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetPrincipal(principalId);

        PostEntity postEntity = validator.validateAndGetPost(createBiddingDto.getPostId());
        CurrencyEntity currencyEntity = validator.validateAndGetCurrency(createBiddingDto.getCurrencyCode());
        PostStateEntity postStateEntity = postEntity.getPostState();

        if (!Objects.equals(
                postStateEntity.getPostStatus().getStatus(), PostStatusEnum.POST_INIT.name())) {
            throw new ResponseStatusException(CONFLICT, "Create bidding: post is not in POST_INIT state");
        }

        Optional<BiddingEntity> biddingEntityOpt = biddingDbService.findByPostAndConsultant(postEntity, principal);

        if (biddingEntityOpt.isPresent()) {
            throw new ResponseStatusException(CONFLICT, "Create bidding: bidding for consultant '" + principal.getUserId() + "' already exists.");
        }

        BiddingEntity biddingToSave = BiddingEntity.builder()
                .post(postEntity)
                .consultant(principal)
                .askPrice(createBiddingDto.getAskPrice())
                .currency(currencyEntity)
                .build();
        BiddingEntity savedBidding = biddingDbService.saveBidding(biddingToSave);
        biddingNotificationService.sendBiddingAddedNotification(savedBidding);

        URI uri = UriComponentsBuilder
                .fromPath(("/bidding/{id}"))
                .buildAndExpand(savedBidding.getBiddingId()).toUri();

        return created(uri).body(
                BiddingDto.fromEntity(savedBidding));
    }

    @PutMapping("/{biddingId}")
    public ResponseEntity<BiddingDto> updateBidding(
            @PathVariable UUID biddingId,
            @Valid @RequestBody UpdateBiddingDto updateBiddingDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetPrincipal(principalId);

        BiddingEntity biddingEntity = validator.validateAndGetBiddingEntity(biddingId);
        PostEntity postEntity = biddingEntity.getPost();
        PostStateEntity postStateEntity = postEntity.getPostState();

        if (!Objects.equals(
                postStateEntity.getPostStatus().getStatus(), PostStatusEnum.POST_INIT.name())) {
            throw new ResponseStatusException(BAD_REQUEST, "Update bidding: post is not in POST_INIT state");
        }

        if (!biddingEntity.getConsultant().equals(principal)) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        if (!Objects.equals(biddingEntity.getAskPrice(), updateBiddingDto.getAskPrice())) {
            biddingEntity.setAskPrice(updateBiddingDto.getAskPrice());
            biddingDbService.saveBidding(biddingEntity);
            biddingNotificationService.sendBiddingChangedNotification(biddingEntity);
        }

        return ok(BiddingDto.fromEntity(biddingEntity));
    }

    @DeleteMapping("/{biddingId}")
    public ResponseEntity<Void> deleteBidding(@PathVariable UUID biddingId) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetPrincipal(principalId);

        BiddingEntity biddingEntity = validator.validateAndGetBiddingEntity(biddingId);
        PostEntity postEntity = biddingEntity.getPost();
        PostStateEntity postStateEntity = postEntity.getPostState();

        if (!Objects.equals(biddingEntity.getConsultant(), principal)) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        if (!Objects.equals(
                postStateEntity.getPostStatus().getStatus(), PostStatusEnum.POST_INIT.name())) {
            throw new ResponseStatusException(BAD_REQUEST, "Update bidding: post is not in POST_INIT state");
        }

        biddingDbService.removeBidding(biddingEntity);
        biddingNotificationService.sendBiddingRemovedNotification(biddingEntity);

        return noContent().build();
    }

}
