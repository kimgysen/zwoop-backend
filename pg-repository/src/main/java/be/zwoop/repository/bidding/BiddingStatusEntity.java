package be.zwoop.repository.bidding;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "\"BiddingStatus\"")
@Entity
@NoArgsConstructor
@Data
public class BiddingStatusEntity {
    @Id
    @Column(name = "bidding_status_id")
    private int biddingStatusId;

    @Column(name = "bidding_status")
    private String biddingStatus;
}