package be.zwoop.features.chatroom.repository.cassandra;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;
import java.util.List;

public interface ChatRoomMessageRepository extends CassandraRepository<ChatRoomMessageEntity, ChatRoomMessagePrimaryKey> {

    List<ChatRoomMessageEntity> findFirst20ByPkChatRoomIdOrderByPkDateDesc(String ChatRoomId);
    Slice<ChatRoomMessageEntity> findAllByPkChatRoomIdEqualsAndPkDateGreaterThan(Pageable pageable, String chatroomId, Date date);

}
