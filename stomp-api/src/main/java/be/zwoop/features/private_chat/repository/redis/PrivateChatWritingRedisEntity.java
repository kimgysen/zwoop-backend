package be.zwoop.features.private_chat.repository.redis;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@Setter
@Builder
@RedisHash("private_chat_writing")
public class PrivateChatWritingRedisEntity {

    @Id
    private String id;

    private Set<WritingToUserRedisEntity> isWritingToUsers = new HashSet<>();

    public void addPartner(WritingToUserRedisEntity partner) {
        if (this.isWritingToUsers == null) {
            this.isWritingToUsers = new HashSet<>(){{
                add(partner);
            }};
        } else {
            this.isWritingToUsers.add(partner);
        }
    }

    public void removePartner(WritingToUserRedisEntity partner) {
        if (this.isWritingToUsers != null) {
            this.isWritingToUsers.remove(partner);
        }
    }

    public void removeAllPartners() {
        this.isWritingToUsers = new HashSet<>();
    }

    public boolean containsPartner(String postId, String partnerId){
        return Stream.ofNullable(this.isWritingToUsers)
                .flatMap(Collection::stream)
                .anyMatch(partner ->
                        partner.getPostId().equals(postId)
                            && partner.getPartnerId().equals(partnerId)
                );
    }

}
