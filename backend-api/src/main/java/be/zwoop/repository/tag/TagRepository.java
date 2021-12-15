package be.zwoop.repository.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    List<TagEntity> findAllByTagIdIn(List<Long> tagIds);
}
