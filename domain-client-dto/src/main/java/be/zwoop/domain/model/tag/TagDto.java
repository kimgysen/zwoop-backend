package be.zwoop.domain.model.tag;

import be.zwoop.repository.tag.TagEntity;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Builder
@Data
public class TagDto implements Serializable {
    long tagId;
    String tagName;

    public static List<TagDto> fromTagList(List<TagEntity> tagEntityList) {
        return tagEntityList
                .stream()
                .map(tagEntity -> TagDto.builder()
                        .tagId(tagEntity.getTagId())
                        .tagName(tagEntity.getTagName())
                        .build())
                .collect(toList());
    }

}
