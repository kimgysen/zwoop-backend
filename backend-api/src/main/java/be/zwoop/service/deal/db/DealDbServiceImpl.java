package be.zwoop.service.deal.db;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.deal.DealRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.service.poststate.PostStateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DealDbServiceImpl implements DealDbService {

    private final DealRepository dealRepository;
    private final UserRepository userRepository;
    private final PostStateService postStateService;


    @Override
    public List<DealEntity> findOpenDealsForUser(UUID userId) {
        Optional<UserEntity> userOpt = userRepository.findByUserIdAndBlockedAndActive(userId, false, true);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found.");
        }

        return dealRepository.findOpenDealsForUser(userOpt.get());
    }

    @Override
    @Transactional
    public DealEntity saveDeal(BiddingEntity biddingEntity) {
        DealEntity dealEntity = DealEntity.builder()
                .bidding(biddingEntity)
                .build();
        DealEntity saved = dealRepository.saveAndFlush(dealEntity);
        postStateService.saveInitDealState(biddingEntity.getPost(), dealEntity);
        return saved;
    }

    public void removeDeal(DealEntity deal) {
        postStateService.rollbackInitDealState(deal.getBidding().getPost());
        dealRepository.delete(deal);
    }

}
