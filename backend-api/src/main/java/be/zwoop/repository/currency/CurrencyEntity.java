package be.zwoop.repository.currency;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "\"Currency\"")
@Entity
@NoArgsConstructor
@Data
public class CurrencyEntity {
    @Id
    @Column(name = "currency_id")
    private int currencyId;

    @Column(name = "currency")
    private String currency;
}