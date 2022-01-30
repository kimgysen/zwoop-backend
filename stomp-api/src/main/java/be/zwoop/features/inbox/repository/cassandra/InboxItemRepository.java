package be.zwoop.features.inbox.repository.cassandra;


import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboxItemRepository extends CassandraRepository<InboxItemEntity, InboxItemPrimaryKey> {
    List<InboxItemEntity> findAllByPkPostIdEqualsAndPkUserIdEqualsOrderByPkLastMessageDateDesc(String postId, String userId);
    List<InboxItemEntity> findFirst20ByPkUserIdEquals(String userId);
}
