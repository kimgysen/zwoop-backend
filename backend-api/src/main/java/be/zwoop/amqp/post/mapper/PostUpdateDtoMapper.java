package be.zwoop.amqp.post.mapper;

import be.zwoop.amqp.domain.common.TagDto;
import be.zwoop.amqp.domain.post.PostUpdateFeatureDto;
import be.zwoop.amqp.domain.post.feature.post.PostChangedDto;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.tag.TagEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static be.zwoop.amqp.domain.post.PostUpdateType.POST_CHANGED;
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

    public PostUpdateFeatureDto<PostChangedDto> mapEntityToTopicDto(UUID postId, PostEntity postEntity) {
        PostChangedDto postChangedDto = PostChangedDto.builder()
                .nickName(postEntity.getAsker().getNickName())
                .postTitle(postEntity.getPostTitle())
                .postText(postEntity.getPostText())
                .bidPrice(postEntity.getBidPrice())
                .tags(mapTagEntityListToDtoList(postEntity.getTags()))
                .build();
        return PostUpdateFeatureDto.<PostChangedDto>builder()
                .postId(postId)
                .postUpdateType(POST_CHANGED)
                .postUpdateDto(postChangedDto)
                .build();
    }

}
