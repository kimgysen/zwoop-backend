package be.zwoop.amqp.domain.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class CurrencyDto  implements Serializable {
    private int currencyId;
    private String currencyCode;
}
