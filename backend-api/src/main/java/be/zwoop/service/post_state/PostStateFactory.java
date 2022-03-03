package be.zwoop.service.post_state;

import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.post_status.PostStatusRepository;
import be.zwoop.repository.poststate.PostStateEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PostStateFactory {
    private final PostStatusRepository postStatusRepository;

    public PostStateEntity buildPostState(PostEntity postEntity, PostStatusEnum postStatusEnum) {
        PostStatusEntity postStatus = postStatusRepository.findByPostStatusId(postStatusEnum.getValue());
        return PostStateEntity.builder()
                .post(postEntity)
                .postStatus(postStatus)
                .build();
    }

}
