package be.zwoop.repository.poststate;


import be.zwoop.repository.BaseEntity;
import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.poststatus.PostStatusEntity;
import be.zwoop.repository.review.ReviewEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Table(name = "\"PostState\"")
@Entity
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
public class PostStateEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_state_id")
    private UUID postStateId;

    @NotNull
    @OneToOne
    @MapsId
    @JoinColumn(name = "post_id")
    @JsonIgnore
    @ToString.Exclude
    private PostEntity post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_status_id")
    private PostStatusEntity postStatus;

    @OneToOne
    @JoinColumn(name = "deal_id")
    private DealEntity deal;

    @OneToOne
    @JoinColumn(name = "answer_id")
    private AnswerEntity answer;

    @OneToOne
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

}
