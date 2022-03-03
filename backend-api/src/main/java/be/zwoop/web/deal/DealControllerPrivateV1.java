package be.zwoop.web.deal;

import be.zwoop.amqp.domain.notification.feature.deal.DealInitDto;
import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.poststate.PostStateEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.service.deal.DealFactory;
import be.zwoop.service.deal.db.DealDbService;
import be.zwoop.service.deal.notification.DealNotificationService;
import be.zwoop.web.deal.dto.CreateDealDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/deal")
public class DealControllerPrivateV1 {

    private final AuthenticationFacade authenticationFacade;
    private final DealControllerValidator validator;
    private final DealFactory dealFactory;
    private final DealDbService dealDbService;
    private final DealNotificationService dealNotificationService;


    @GetMapping("/init")
    public List<DealInitDto> getPrincipalInitDeals() {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        List<DealEntity> dealEntities = dealDbService.findOpenDealsForUser(principalId);
        return dealFactory.buildDealInitDtos(dealEntities);
    }

    @PostMapping
    public ResponseEntity<Void> createDeal(@Valid @RequestBody CreateDealDto createDealDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetPrincipal(principalId);

        BiddingEntity biddingEntity = validator.validateAndGetBiddingEntity(createDealDto.getBiddingId());
        PostEntity postEntity = biddingEntity.getPost();
        PostStateEntity postStateEntity = postEntity.getPostState();

        if (!Objects.equals(postEntity.getOp(), principal)) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        if (!Objects.equals(
                postStateEntity.getPostStatus().getStatus(), PostStatusEnum.POST_INIT.name())) {
            throw new ResponseStatusException(CONFLICT, "Create deal: post is not in POST_INIT state");
        }

        if (postStateEntity.getDeal() != null) {
            throw new ResponseStatusException(CONFLICT, "Create deal: Bidding already has a deal with id: " + postStateEntity.getDeal().getDealId());
        }

        DealEntity savedDeal = dealDbService.saveDeal(biddingEntity);
        dealNotificationService.sendDealInitNotification(savedDeal);

        URI uri = UriComponentsBuilder
                .fromPath(("/deal/{id}"))
                .buildAndExpand(savedDeal.getDealId()).toUri();

        return created(uri).build();
    }

    @DeleteMapping("/{dealId}")
    public ResponseEntity<Void> cancelDeal(@PathVariable UUID dealId) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetPrincipal(principalId);

        DealEntity dealEntity = validator.validateAndGetDealEntity(dealId);
        BiddingEntity biddingEntity = dealEntity.getBidding();
        PostEntity postEntity = biddingEntity.getPost();
        PostStateEntity postStateEntity = postEntity.getPostState();

        if (!Objects.equals(dealEntity.getBidding().getPost().getOp(), principal)) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        if (!Objects.equals(
                postStateEntity.getPostStatus().getStatus(), PostStatusEnum.DEAL_INIT.name())) {
            throw new ResponseStatusException(CONFLICT, "Delete deal: post is not in DEAL_INIT state");
        }

        dealDbService.removeDeal(dealEntity);
        dealNotificationService.sendDealRemovedNotification(dealEntity);

        return noContent().build();
    }

}
