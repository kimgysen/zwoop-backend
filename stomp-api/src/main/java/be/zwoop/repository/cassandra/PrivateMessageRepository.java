package be.zwoop.repository.cassandra;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;

public interface PrivateMessageRepository extends CassandraRepository<PrivateMessage, MessagePrimaryKey> {

    Slice<PrivateMessage> findAllByPkChatRoomIdEqualsAndPkUserIdEqualsAndFromUserIdEqualsOrToUserIdEqualsAndPkDateGreaterThan(Pageable pageable, String chatroomId, String userId, String chatPartnerUserFrom, String chatPartnerUserTo,Date date);

}
