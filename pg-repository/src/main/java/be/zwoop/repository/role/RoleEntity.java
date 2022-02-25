package be.zwoop.repository.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "\"Role\"")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoleEntity {

    @Id
    @Column(name = "role_id")
    int roleId;

    @Column(name = "role")
    String role;

}
