package be.zwoop.repository.post;

import be.zwoop.repository.BaseEntity;
import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.application.ApplicationEntity;
import be.zwoop.repository.currency.CurrencyEntity;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
    @GeneratedValue
    @Column(name = "post_id")
    private UUID postId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "asker_id")
    private UserEntity asker;

    @NotNull
    @Column(name = "post_title")
    private String postTitle;

    @NotNull
    @Column(name = "post_text")
    private String postText;

    @Column(name = "offer_price")
    private double offerPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id", updatable = false, insertable = false)
    private CurrencyEntity currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_status_id", updatable = false, insertable = false)
    private PostStatusEntity postStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(
            name = "\"Post_Tag\"",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private List<TagEntity> tags;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<AnswerEntity> answers;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ApplicationEntity> applications;

}
