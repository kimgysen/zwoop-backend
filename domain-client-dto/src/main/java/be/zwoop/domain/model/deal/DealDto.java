package be.zwoop.domain.model.deal;

import be.zwoop.domain.model.user.UserDto;
import be.zwoop.repository.deal.DealEntity;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;


@Getter
@Builder
public class DealDto implements Serializable {
    private final UUID dealId;
    private final UserDto op;
    private final UserDto consultant;
    private final UUID postId;
    private final String postTitle;
    private final BigDecimal dealPrice;
    private final String currencyCode;
    private final LocalDateTime createdAt;

    public static DealDto fromEntity(DealEntity dealEntity) {
        return DealDto.builder()
                    .dealId(dealEntity.getDealId())
                    .op(UserDto.fromUserEntity(dealEntity.getBidding().getPost().getOp()))
                    .consultant(UserDto.fromUserEntity(dealEntity.getBidding().getConsultant()))
                    .postId(dealEntity.getBidding().getPost().getPostId())
                    .postTitle(dealEntity.getBidding().getPost().getPostTitle())
                    .dealPrice(dealEntity.getBidding().getAskPrice())
                    .createdAt(dealEntity.getCreatedAt())
                    .build();
    }

    public static List<DealDto> fromEntityList(List<DealEntity> dealEntityList) {
        return dealEntityList.stream()
                .map(DealDto::fromEntity)
                .collect(toList());
    }
}
