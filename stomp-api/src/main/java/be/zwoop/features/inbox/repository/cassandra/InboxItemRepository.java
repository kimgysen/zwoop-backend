package be.zwoop.features.inbox.repository.cassandra;


import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboxItemRepository extends CassandraRepository<InboxItemEntity, InboxItemPrimaryKey> {
    Optional<InboxItemEntity> findByPkPostIdEqualsAndPkUserIdEquals(String postId, String userId);
    List<InboxItemEntity> findAllByPkPostIdEqualsAndPkUserIdEqualsOrderByPkLastMessageDateDesc(String postId, String userId);
    List<InboxItemEntity> findAllByPkUserIdEqualsOrderByPkLastMessageDateDesc(String userId);
}
