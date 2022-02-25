package be.zwoop.amqp.post.mapper;

import be.zwoop.amqp.domain.model.TagDto;
import be.zwoop.amqp.domain.model.UserDto;
import be.zwoop.amqp.domain.post_update.PostUpdateDto;
import be.zwoop.amqp.domain.post_update.feature.post.PostChangedDto;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.user.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static be.zwoop.amqp.domain.post_update.PostUpdateType.POST_CHANGED;
import static java.util.stream.Collectors.toList;

@Component
public class PostUpdateDtoMapper {

    private List<TagDto> mapTagEntityListToDtoList(List<TagEntity> tagEntityList) {
        return tagEntityList.stream()
                .map(tagEntity -> TagDto
                        .builder()
                        .tagId(tagEntity.getTagId())
                        .tagName(tagEntity.getTagName())
                        .build())
                .collect(toList());
    }

    public PostUpdateDto<PostChangedDto> mapEntityToTopicDto(UUID postId, PostEntity postEntity) {
        UserEntity opEntity = postEntity.getOp();
        PostChangedDto postChangedDto = PostChangedDto.builder()
                .op(UserDto.builder()
                        .userId(opEntity.getUserId())
                        .nickName(opEntity.getNickName())
                        .avatar(opEntity.getProfilePic())
                        .build())
                .postTitle(postEntity.getPostTitle())
                .postText(postEntity.getPostText())
                .bidPrice(postEntity.getBidPrice())
                .tags(mapTagEntityListToDtoList(postEntity.getTags()))
                .build();
        return PostUpdateDto.<PostChangedDto>builder()
                .postId(postId)
                .postUpdateType(POST_CHANGED)
                .dto(postChangedDto)
                .build();
    }

}
