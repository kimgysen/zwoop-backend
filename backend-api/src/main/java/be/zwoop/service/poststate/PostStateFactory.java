package be.zwoop.service.poststate;

import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.poststate.PostStateEntity;
import be.zwoop.repository.poststatus.PostStatusEntity;
import be.zwoop.repository.poststatus.PostStatusRepository;
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
