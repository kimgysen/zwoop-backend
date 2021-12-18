package be.zwoop.repository.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    @Query("FROM TagEntity t WHERE t.tagName LIKE :tagName%")
    List<TagEntity> findByTagNameLike(@Param("tagName") String tagName);

    List<TagEntity> findAllByTagIdIn(List<Long> tagIds);
}
