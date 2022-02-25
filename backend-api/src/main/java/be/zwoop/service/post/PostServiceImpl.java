package be.zwoop.service.post;


import be.zwoop.amqp.post.PostNotificationSender;
import be.zwoop.amqp.post.mapper.PostUpdateDtoMapper;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post.PostRepository;
import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.poststate.PostStateEntity;
import be.zwoop.repository.poststate.PostStateRepository;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.tag.TagRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.web.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostFactory postFactory;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostStateRepository postStateRepository;
    private final TagRepository tagRepository;
    private final PostUpdateDtoMapper postUpdateDtoMapper;
    private final PostNotificationSender postNotificationSender;


    @Override
    public Optional<UserEntity> findByUserId(UUID userId) {
        return userRepository.findByUserIdAndBlockedAndActive(userId, false, true);
    }

    @Override
    public Optional<PostEntity> findByTitleAndOp(String title, UserEntity opEntity) {
        return postRepository.findByPostTitleAndOp(title, opEntity);
    }

    @Override
    public Optional<PostEntity> findByPostId(UUID postId) {
        return postRepository.findById(postId);
    }

    @Override
    public Page<PostEntity> getFeed(PostStatusEntity postStatus, Pageable pageable) {
        return postRepository.findAllByPostState_PostStatusEqualsOrderByCreatedAtDesc(postStatus, pageable);
    }

    @Override
    public Page<PostEntity> getFeedByTag(TagEntity tagEntity, PostStatusEntity postStatus, Pageable pageable) {
        return postRepository.findAllByTagsContainingAndPostState_PostStatusEqualsOrderByCreatedAtDesc(tagEntity, postStatus, pageable);
    }

    @Override
    @Transactional
    public PostEntity createPost(PostDto postDto, UserEntity opEntity) {
        PostEntity postEntity = postFactory.buildPostFromDto(postDto, opEntity);
        postRepository.saveAndFlush(postEntity);
        PostStateEntity postStateEntity = postFactory.buildInitPostState(postEntity);
        postStateRepository.saveAndFlush(postStateEntity);
        return postEntity;
    }

    @Override
    public void updatePost(PostEntity toUpdate, PostDto postDto) {
        toUpdate.setPostTitle(postDto.getTitle());
        toUpdate.setPostText(postDto.getText());
        List<TagEntity> tagEntities = tagRepository.findAllByTagIdIn(
                postFactory.collectTagIdsByPostDto(postDto));
        toUpdate.setTags(tagEntities);

        postRepository.saveAndFlush(toUpdate);
    }

    @Override
    public void sendPostChangedNotification(PostEntity toUpdate) {
        try {
            postNotificationSender.sendPostEventNotification(
                    postUpdateDtoMapper.mapEntityToTopicDto(
                                    toUpdate.getPostId(), toUpdate));
        } catch(Exception e) {
            log.error("Error sending queue update for post: " + toUpdate.getPostId()
                    + " with error: " + e.getMessage());
        }
    }

    @Override
    public boolean hasPostChanged(PostEntity postEntity, PostDto postDto) {
        return !postEntity.getPostTitle().equals(postDto.getTitle())
                || !postEntity.getPostText().equals(postDto.getText())
                || !postEntity.getBidPrice().equals(postDto.getBidPrice())
                || !collectTagIdsByPostEntity(postEntity)
                        .equals(postFactory.collectTagIdsByPostDto(postDto));
    }

    private List<Long> collectTagIdsByPostEntity(PostEntity postEntity) {
        return postEntity.getTags()
                .stream()
                .map(TagEntity::getTagId)
                .collect(toList());
    }

}
