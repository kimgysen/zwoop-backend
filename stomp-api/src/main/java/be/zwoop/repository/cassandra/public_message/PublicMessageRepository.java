package be.zwoop.repository.cassandra.public_message;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;
import java.util.List;

public interface PublicMessageRepository extends CassandraRepository<PublicMessageEntity, PublicMessagePrimaryKey> {

    List<PublicMessageEntity> findFirst20ByPkChatRoomIdOrderByPkDateDesc(String ChatRoomId);
    Slice<PublicMessageEntity> findAllByPkChatRoomIdEqualsAndPkDateGreaterThan(Pageable pageable, String chatroomId, Date date);

}
