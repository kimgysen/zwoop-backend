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
    @Column(name = "review_id")
    private UUID reviewId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "op_id")
    private UserEntity op;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "consultant_id")
    private UserEntity consultant;

    @NotNull
    @Column(name = "review_text")
    private String reviewText;

}
