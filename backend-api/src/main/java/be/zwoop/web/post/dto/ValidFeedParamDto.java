package be.zwoop.web.post.dto;

import be.zwoop.domain.enum_type.PostFeedTypeEnum;
import be.zwoop.repository.tag.TagEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@NoArgsConstructor
public class ValidFeedParamDto {

    public ValidFeedParamDto(PostFeedTypeEnum feedType) {
        this.feedType = feedType;
    }

    private PostFeedTypeEnum feedType;

    @Nullable
    private List<TagEntity> tagsEntityList;

    @Nullable
    private TagEntity tagEntity;

}
