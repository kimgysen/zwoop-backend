package be.zwoop.web.deal;

import be.zwoop.amqp.domain.notification.feature.deal.DealOpenedDto;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.service.deal.DealFactory;
import be.zwoop.service.deal.DealService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/deal")
public class DealControllerPrivateV1 {

    private final AuthenticationFacade authenticationFacade;
    private final DealFactory dealFactory;
    private final DealService dealService;


    @GetMapping("/open")
    public List<DealOpenedDto> getOpenDeals() {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        List<DealEntity> dealEntities = dealService.findOpenDealsForUser(principalId);
        return dealFactory.fromDealEntities(dealEntities);
    }

}
