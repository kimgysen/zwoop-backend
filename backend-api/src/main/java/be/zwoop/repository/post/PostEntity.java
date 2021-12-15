package be.zwoop.repository.post;

import be.zwoop.repository.BaseEntity;
import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.application.ApplicationEntity;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;


@Table(name = "\"Post\"")
@Entity
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
public class PostEntity extends BaseEntity {

    @Id
    @Column(name = "post_id")
    private UUID postId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "asker_id", updatable = false, insertable = false)
    private UserEntity asker;

    @NotNull
    @Column(name = "post_title")
    private String postTitle;

    @NotNull
    @Column(name = "post_text")
    private String postText;

    @NotNull
    @Column(name = "bid_price")
    private double bidPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_status_id", updatable = false, insertable = false)
    private PostStatusEntity postStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "\"Post_Tag\"",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private List<TagEntity> tags;

    @OneToMany(mappedBy = "PostEntity", fetch = FetchType.EAGER)
    private List<AnswerEntity> answers;

    @OneToMany(mappedBy = "PostEntity", fetch = FetchType.EAGER)
    private List<ApplicationEntity> applications;

}
