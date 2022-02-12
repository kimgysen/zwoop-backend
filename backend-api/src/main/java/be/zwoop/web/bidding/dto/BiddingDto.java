package be.zwoop.web.bidding.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class BiddingDto {
    @NotNull
    private BigDecimal askPrice;

    @NotNull
    private String currencyCode;
}
