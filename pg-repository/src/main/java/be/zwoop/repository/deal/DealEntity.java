package be.zwoop.repository.deal;

import be.zwoop.repository.BaseEntity;
import be.zwoop.repository.currency.CurrencyEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "\"Deal\"")
@Entity
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
public class DealEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "deal_id")
    private UUID dealId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "respondent_id")
    private UserEntity respondent;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "asker_id")
    private UserEntity asker;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deal_status_id")
    private DealStatusEntity dealStatus;

    @NotNull
    @Column(name = "deal_price")
    private BigDecimal dealPrice;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currency;

}
