package be.zwoop.web.tag.dto;

import be.zwoop.repository.tag.TagEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class IsWatchingDto {
    boolean isWatching;
    TagEntity tag;

    @JsonProperty("isWatching")
    public boolean isWatching() {
        return isWatching;
    }

    public TagEntity getTag() {
        return tag;
    }

}
