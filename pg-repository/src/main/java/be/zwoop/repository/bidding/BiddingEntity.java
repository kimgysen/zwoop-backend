package be.zwoop.repository.bidding;

import be.zwoop.repository.BaseEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Table(name = "\"Bidding\"")
@Entity
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
public class BiddingEntity extends BaseEntity {

    @Id
    @Column(name = "bidding_id")
    private UUID biddingId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "respondent_id", updatable = false, insertable = false)
    private UserEntity respondent;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bidding_status_id", updatable = false, insertable = false)
    private BiddingStatusEntity biddingStatus;

    @Column(name = "ask_price")
    private double askPrice;

}
