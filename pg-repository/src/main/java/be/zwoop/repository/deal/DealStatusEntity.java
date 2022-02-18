package be.zwoop.repository.deal;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "\"DealStatus\"")
@Entity
@NoArgsConstructor
@Data
public class DealStatusEntity {
    @Id
    @Column(name = "deal_status_id")
    private int dealStatusId;

    @Column(name = "deal_status")
    private String dealStatus;
}
