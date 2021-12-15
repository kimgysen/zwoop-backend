package be.zwoop.repository.answer;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "\"AnswerStatus\"")
@Entity
@NoArgsConstructor
@Data
public class AnswerStatusEntity {
    @Id
    @Column(name = "answer_status_id")
    private int answerStatusId;

    @Column(name = "answer_status")
    private String answerStatus;
}