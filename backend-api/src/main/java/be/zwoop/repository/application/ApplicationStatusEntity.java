package be.zwoop.repository.application;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "\"ApplicationStatus\"")
@Entity
@NoArgsConstructor
@Data
public class ApplicationStatusEntity {
    @Id
    @Column(name = "application_status_id")
    private int applicationStatusId;

    @Column(name = "application_status")
    private String applicationStatus;
}