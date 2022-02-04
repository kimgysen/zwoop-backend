package be.zwoop.web.bidding.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BiddingDto {
    @NotNull
    private double askPrice;
}
