package be.zwoop.repository.answer;

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

@Table(name = "\"Answer\"")
@Entity
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
public class AnswerEntity extends BaseEntity {
    @Id
    @Column(name = "answer_id")
    private UUID answerId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "respondent_id", updatable = false, insertable = false)
    private UserEntity respondent;

    @NotNull
    @Column(name = "answer_text")
    private String answerText;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "answer_status_id", updatable = false, insertable = false)
    private AnswerStatusEntity answerStatus;

    @Column(name = "closing_price")
    private double closingPrice;

}
