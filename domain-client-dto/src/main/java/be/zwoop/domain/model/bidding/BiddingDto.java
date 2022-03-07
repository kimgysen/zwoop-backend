package be.zwoop.domain.model.bidding;

import be.zwoop.domain.model.user.UserDto;
import be.zwoop.repository.bidding.BiddingEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@SuperBuilder
public class BiddingDto implements Serializable {
    private final UUID biddingId;
    private final UserDto op;
    private final UserDto consultant;
    private final BigDecimal askPrice;
    private final String currencyCode;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public static BiddingDto fromEntity(BiddingEntity biddingEntity) {
        UserDto op = UserDto.fromUserEntity(biddingEntity.getPost().getOp());
        UserDto consultant = UserDto.fromUserEntity(biddingEntity.getConsultant());

        return BiddingDto.builder()
                    .biddingId(biddingEntity.getBiddingId())
                    .op(op)
                    .consultant(consultant)
                    .currencyCode(biddingEntity.getCurrency().getCurrencyCode())
                    .askPrice(biddingEntity.getAskPrice())
                    .createdAt(biddingEntity.getCreatedAt())
                    .updatedAt(biddingEntity.getUpdatedAt())
                    .build();
    }
}
