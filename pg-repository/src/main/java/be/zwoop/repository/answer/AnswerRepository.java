package be.zwoop.repository.answer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnswerRepository extends JpaRepository<AnswerEntity, UUID> {
}
