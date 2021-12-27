package be.zwoop.repository.cassandra;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;

public interface PublicMessageRepository extends CassandraRepository<PublicMessage, MessagePrimaryKey> {

    Slice<PublicMessage> findAllByPkChatRoomIdEqualsAndPkDateGreaterThan(Pageable pageable, String chatroomId, Date date);

}
