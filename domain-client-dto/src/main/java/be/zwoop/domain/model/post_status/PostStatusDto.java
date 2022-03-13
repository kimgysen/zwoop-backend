package be.zwoop.domain.model.post_status;

import be.zwoop.repository.poststatus.PostStatusEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostStatusDto {
    private final String status;
    private final String description;

    public static PostStatusDto fromEntity(PostStatusEntity postStatusEntity) {
        return PostStatusDto
                .builder()
                .status(postStatusEntity.getStatus())
                .description(postStatusEntity.getDescription())
                .build();
    }
}
