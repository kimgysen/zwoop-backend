package be.zwoop.service.post;


import be.zwoop.amqp.domain.common.TagDto;
import be.zwoop.amqp.post.PostUpdateSender;
import be.zwoop.amqp.post.mapper.PostUpdateDtoMapper;
import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.currency.CurrencyEntity;
import be.zwoop.repository.currency.CurrencyRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post.PostRepository;
import be.zwoop.repository.post.PostStatusEntity;
import be.zwoop.repository.post.PostStatusRepository;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.tag.TagRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.web.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CurrencyRepository currencyRepository;
    private final PostStatusRepository postStatusRepository;
    private final TagRepository tagRepository;
    private final PostUpdateDtoMapper postUpdateDtoMapper;
    private final PostUpdateSender postUpdateSender;


    @Override
    public Optional<UserEntity> findAskerByUserId(UUID userId) {
        return userRepository.findByUserIdAndBlockedAndActive(userId, false, true);
    }

    @Override
    public Optional<PostEntity> findByTitleAndAsker(String title, UserEntity askerEntity) {
        return postRepository.findByPostTitleAndAsker(title, askerEntity);
    }

    @Override
    public Optional<PostEntity> findByPostId(UUID postId) {
        return postRepository.findById(postId);
    }

    @Override
    public PostEntity createPost(PostDto postDto, UserEntity askerEntity) {
        Optional<CurrencyEntity> currencyEntityOpt = currencyRepository.findByCurrencyCode(postDto.getCurrencyCode());

        if (currencyEntityOpt.isEmpty())
            throw new ResponseStatusException(BAD_REQUEST, "Currency not found");

        CurrencyEntity currencyEntity = currencyEntityOpt.get();

        List<TagEntity> tagEntities = tagRepository.findAllByTagIdIn(
                collectTagIdsByPostDto(postDto));

        PostStatusEntity postStatusEntity = postStatusRepository
                .findById(PostStatusEnum.OPEN.getValue())
                .orElse(null);

        PostEntity toSave = PostEntity.builder()
                .asker(askerEntity)
                .postStatus(postStatusEntity)
                .postTitle(postDto.getTitle())
                .postText(postDto.getText())
                .bidPrice(postDto.getBidPrice())
                .currency(currencyEntity)
                .postStatus(postStatusEntity)
                .tags(tagEntities)
                .build();

        return postRepository.saveAndFlush(toSave);
    }

    @Override
    public void updatePost(PostEntity toUpdate, PostDto postDto) {
        toUpdate.setPostTitle(postDto.getTitle());
        toUpdate.setPostText(postDto.getText());
        List<TagEntity> tagEntities = tagRepository.findAllByTagIdIn(
                collectTagIdsByPostDto(postDto));
        toUpdate.setTags(tagEntities);
        postRepository.saveAndFlush(toUpdate);
    }

    @Override
    public void sendPostChangedToQueue(PostEntity toUpdate) {
        try {
            postUpdateSender.sendToQueue(
                    postUpdateDtoMapper.mapEntityToTopicDto(
                                    toUpdate.getPostId(), toUpdate));
        } catch(Exception e) {
            log.error("Error sending queue update for post: " + toUpdate.getPostId()
                    + " with error: " + e.getMessage());
        }
    };

    @Override
    public boolean hasPostChanged(PostEntity postEntity, PostDto postDto) {
        return !postEntity.getPostTitle().equals(postDto.getTitle())
                || !postEntity.getPostText().equals(postDto.getText())
                || !postEntity.getBidPrice().equals(postDto.getBidPrice())
                || !collectTagIdsByPostEntity(postEntity)
                        .equals(collectTagIdsByPostDto(postDto));
    }

    private List<Long> collectTagIdsByPostEntity(PostEntity postEntity) {
        return postEntity.getTags()
                .stream()
                .map(TagEntity::getTagId)
                .collect(toList());
    }

    private List<Long> collectTagIdsByPostDto(PostDto postDto) {
        return postDto.getTags()
                .stream()
                .map(TagDto::getTagId)
                .collect(toList());
    }

}
