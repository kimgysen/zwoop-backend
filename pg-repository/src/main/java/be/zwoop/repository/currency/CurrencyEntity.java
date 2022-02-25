package be.zwoop.repository.currency;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "\"Currency\"")
@Entity
@NoArgsConstructor
@Data
public class CurrencyEntity {
    @Id
    @Column(name = "currency_id")
    private int currencyId;

    @Column(name = "currency_code")
    private String currencyCode;
}
