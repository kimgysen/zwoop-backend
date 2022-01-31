package be.zwoop.features.inbox.repository.cassandra;


import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboxItemRepository extends CassandraRepository<InboxItemEntity, InboxItemPrimaryKey> {
    List<InboxItemEntity> findAllByPkPostIdEqualsAndPkUserIdEquals(String postId, String userId);
    Optional<InboxItemEntity> findByPkPostIdEqualsAndPkUserIdEqualsAndPkPartnerIdEquals(String postId, String userId, String partnerId);
    List<InboxItemEntity> findFirst20ByPkUserIdEquals(String userId);
}
