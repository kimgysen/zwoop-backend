package be.zwoop.repository.tag;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "\"Tag\"")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TagEntity {
    @Id
    @Column(name = "tag_id")
    long tagId;

    @Column(name = "tag")
    String tagName;

}
