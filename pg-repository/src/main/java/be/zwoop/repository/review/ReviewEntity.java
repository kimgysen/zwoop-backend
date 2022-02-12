package be.zwoop.repository.review;

import be.zwoop.repository.BaseEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Table(name = "\"Review\"")
@Entity
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
public class ReviewEntity extends BaseEntity {

    @Id
    @Column
    private UUID reviewId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private UserEntity reviewer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "reviewee_id")
    private UserEntity reviewee;

    @NotNull
    @Column(name = "review_text")
    private String reviewText;

}
