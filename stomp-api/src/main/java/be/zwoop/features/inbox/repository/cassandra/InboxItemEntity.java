package be.zwoop.features.inbox.repository.cassandra;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Builder
@Table("inbox_items")
public class InboxItemEntity {

    @PrimaryKey
    private InboxItemPrimaryKey pk;

    private String partnerId;

    private String fromUserId;
    private String fromNickName;
    private String fromAvatar;
    private String toUserId;
    private String toNickName;
    private String toAvatar;

    private int unread = 0;
    private boolean hasPartnerRead;
    private String lastMessage;

}

