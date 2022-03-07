package be.zwoop.amqp.post_notification.mapper;

import be.zwoop.domain.model.post.PostDto;
import be.zwoop.domain.post_update.PostUpdateDto;
import be.zwoop.repository.post.PostEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static be.zwoop.domain.post_update.PostUpdateType.POST_CHANGED;

@Component
public class PostUpdateDtoMapper {

    public PostUpdateDto<PostDto> mapEntityToTopicDto(UUID postId, PostEntity postEntity) {
        return PostUpdateDto.<PostDto>builder()
                .postId(postId)
                .postUpdateType(POST_CHANGED)
                .dto(PostDto.fromEntity(postEntity))
                .build();
    }

}
