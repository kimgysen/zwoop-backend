package be.zwoop.domain.model.post;

import be.zwoop.domain.model.post_state.PostStateDto;
import be.zwoop.domain.model.tag.TagDto;
import be.zwoop.domain.model.user.UserDto;
import be.zwoop.repository.post.PostEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostDto implements Serializable {
    private final UUID postId;
    private final UserDto op;
    private final String postTitle;
    private final String postText;
    private final BigDecimal bidPrice;
    private final String currencyCode;
    private final List<TagDto> tagList;
    private final PostStateDto postState;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static PostDto fromEntity(PostEntity postEntity) {
        return PostDto.builder()
                .postId(postEntity.getPostId())
                .op(UserDto.fromUserEntity(postEntity.getOp()))
                .postTitle(postEntity.getPostTitle())
                .postText(postEntity.getPostText())
                .bidPrice(postEntity.getBidPrice())
                .tagList(TagDto.fromTagList(postEntity.getTags()))
                .currencyCode(postEntity.getCurrency().getCurrencyCode())
                .postState(PostStateDto.fromPostStateDto(postEntity.getPostState()))
                .createdAt(postEntity.getCreatedAt())
                .updatedAt(postEntity.getUpdatedAt())
                .build();
    }

    public static Page<PostDto> fromEntityPage(Page<PostEntity> postEntityPage) {
        List<PostDto> postDtoList = postEntityPage
                .stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(postDtoList);
    }
}
