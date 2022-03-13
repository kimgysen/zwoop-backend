package be.zwoop.repository.poststatus;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "\"PostStatus\"")
@Entity
@NoArgsConstructor
@Data
public class PostStatusEntity {
    @Id
    @Column(name = "post_status_id")
    private int postStatusId;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;
}