package be.zwoop.domain.model.tag;

import be.zwoop.repository.tag.TagEntity;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Builder
@Data
public class TagDto implements Serializable {
    long tagId;
    String tagName;

    public static TagDto fromEntity(TagEntity tagEntity) {
        return TagDto.builder()
                .tagId(tagEntity.getTagId())
                .tagName(tagEntity.getTagName())
                .build();
    }

    public static Set<TagDto> fromTagSet(Set<TagEntity> tagEntitySet) {
        return tagEntitySet
                .stream()
                .map(TagDto::fromEntity)
                .collect(toSet());
    }

    public static List<TagDto> fromTagList(List<TagEntity> tagEntityList) {
        return tagEntityList
                .stream()
                .map(TagDto::fromEntity)
                .collect(toList());
    }

}
