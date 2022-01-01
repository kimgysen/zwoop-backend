package be.zwoop.repository.cassandra;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;
import java.util.List;

public interface PublicMessageRepository extends CassandraRepository<PublicMessage, PublicMessagePrimaryKey> {

    List<PublicMessage> findFirst20ByPkChatRoomIdOrderByPkDateDesc(String ChatRoomId);
    Slice<PublicMessage> findAllByPkChatRoomIdEqualsAndPkDateGreaterThan(Pageable pageable, String chatroomId, Date date);

}
