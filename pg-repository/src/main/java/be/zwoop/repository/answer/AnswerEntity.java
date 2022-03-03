package be.zwoop.repository.answer;

import be.zwoop.repository.BaseEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue
    @Column(name = "answer_id")
    private UUID answerId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private PostEntity post;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "consultant_id")
    private UserEntity consultant;

    @NotNull
    @Column(name = "answer_text")
    private String answerText;

}
