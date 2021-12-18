package be.zwoop.repository.application;

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

@Table(name = "\"Application\"")
@Entity
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
public class ApplicationEntity extends BaseEntity {

    @Id
    @Column(name = "application_id")
    private UUID applicationId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "respondent_id", updatable = false, insertable = false)
    private UserEntity respondentId;

    @NotNull
    @Column(name = "application_text")
    private String applicationText;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_status_id", updatable = false, insertable = false)
    private ApplicationStatusEntity applicationStatusEntity;

    @Column(name = "ask_price")
    private double askPrice;

}
