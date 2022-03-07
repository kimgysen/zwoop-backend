package be.zwoop.service.post.db;


import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post.PostRepository;
import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.tag.TagRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.service.post.PostFactory;
import be.zwoop.service.post_state.PostStateService;
import be.zwoop.web.post.dto.SavePostDto;
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
public class PostDbServiceImpl implements PostDbService {

    private final PostFactory postFactory;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostStateService postStateService;
    private final TagRepository tagRepository;


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
    public PostEntity createPost(SavePostDto savePostDto, UserEntity opEntity) {
        PostEntity postEntity = postFactory.buildPostFromDto(savePostDto, opEntity);
        postRepository.saveAndFlush(postEntity);
        postStateService.saveInitPostState(postEntity);
        return postEntity;
    }

    @Override
    public void updatePost(PostEntity toUpdate, SavePostDto savePostDto) {
        toUpdate.setPostTitle(savePostDto.getTitle());
        toUpdate.setPostText(savePostDto.getText());
        List<TagEntity> tagEntities = tagRepository.findAllByTagIdIn(
                postFactory.collectTagIdsByPostDto(savePostDto));
        toUpdate.setTags(tagEntities);

        postRepository.saveAndFlush(toUpdate);
    }

    @Override
    public boolean hasPostChanged(PostEntity postEntity, SavePostDto savePostDto) {
        return !postEntity.getPostTitle().equals(savePostDto.getTitle())
                || !postEntity.getPostText().equals(savePostDto.getText())
                || !postEntity.getBidPrice().equals(savePostDto.getBidPrice())
                || !collectTagIdsByPostEntity(postEntity)
                        .equals(postFactory.collectTagIdsByPostDto(savePostDto));
    }

    private List<Long> collectTagIdsByPostEntity(PostEntity postEntity) {
        return postEntity.getTags()
                .stream()
                .map(TagEntity::getTagId)
                .collect(toList());
    }

}
