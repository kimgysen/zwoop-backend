package be.zwoop.web.deal;

import be.zwoop.amqp.domain.notification.feature.deal.DealInitDto;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.service.deal.DealFactory;
import be.zwoop.service.deal.DealService;
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

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
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
    private final DealService dealService;


    @GetMapping("/init")
    public List<DealInitDto> getPrincipalInitDeals() {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        List<DealEntity> dealEntities = dealService.findOpenDealsForUser(principalId);
        return dealFactory.buildDealInitDtos(dealEntities);
    }

    @PostMapping
    public ResponseEntity<Void> createDeal(@Valid @RequestBody CreateDealDto createDealDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetUser(principalId);

        BiddingEntity biddingEntity = validator.validateAndGetBiddingEntity(createDealDto.getBiddingId());

        if (!Objects.equals(biddingEntity.getPost().getOp(), principal)) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        DealEntity savedDeal = dealService.saveDeal(biddingEntity);
        dealService.sendDealInitNotification(savedDeal);

        URI uri = UriComponentsBuilder
                .fromPath(("/deal/{id}"))
                .buildAndExpand(savedDeal.getDealId()).toUri();

        return created(uri).build();
    }

    @DeleteMapping("/{dealId}")
    public ResponseEntity<Void> cancelDeal(@PathVariable UUID dealId) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetUser(principalId);

        DealEntity dealEntity = validator.validateAndGetDealEntity(dealId);

        if (!Objects.equals(dealEntity.getBidding().getPost().getOp(), principal)) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        dealService.removeDeal(dealEntity);
        dealService.sendDealRemovedNotification(dealEntity);

        return noContent().build();
    }

}
