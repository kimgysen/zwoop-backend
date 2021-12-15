package be.zwoop.repository.user;

import be.zwoop.repository.tag.TagEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Table(name = "\"User\"")
@Entity
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "profile_pic")
    private String profilePic;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "public_address_trx")
    private String publicAddressTrx;

    @Column(name = "about_text")
    private String aboutText;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "\"User_Tag\"",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<TagEntity> tags;



}
