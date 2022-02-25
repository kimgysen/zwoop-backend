package be.zwoop.web.bidding.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateBiddingDto {

    @NotNull
    private UUID postId;

    @NotNull
    private BigDecimal askPrice;

    @NotNull
    private String currencyCode;
}
