package be.zwoop.repository.bidding;

import be.zwoop.repository.BaseEntity;
import be.zwoop.repository.currency.CurrencyEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "\"Bidding\"")
@Entity
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
public class BiddingEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "bidding_id")
    private UUID biddingId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private PostEntity post;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "consultant_id")
    private UserEntity consultant;

    @Column(name = "ask_price", scale = 3)
    private BigDecimal askPrice;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currency;

}
