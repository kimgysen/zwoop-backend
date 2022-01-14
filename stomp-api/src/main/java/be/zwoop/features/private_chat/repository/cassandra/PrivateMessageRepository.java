package be.zwoop.features.private_chat.repository.cassandra;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;
import java.util.List;


public interface PrivateMessageRepository extends CassandraRepository<PrivateMessageEntity, PrivateMessagePrimaryKey> {
    List<PrivateMessageEntity> findFirst20ByPkPostIdAndPkUserIdEqualsAndPkPartnerIdEqualsOrderByPkDateDesc(String postId, String userId, String partnerId);
    Slice<PrivateMessageEntity> findAllByPkUserIdEqualsAndFromUserIdEqualsOrToUserIdEqualsAndPkDateGreaterThan(Pageable pageable, String userId, String chatPartnerUserFrom, String chatPartnerUserTo, Date date);

}
