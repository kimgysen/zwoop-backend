package be.zwoop.repository.deal;

import be.zwoop.repository.BaseEntity;
import be.zwoop.repository.bidding.BiddingEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @OneToOne
    @JoinColumn(name = "bidding_id")
    private BiddingEntity bidding;

}
