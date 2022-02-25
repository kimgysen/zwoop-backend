package be.zwoop.service.post;

import be.zwoop.amqp.domain.model.TagDto;
import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.currency.CurrencyEntity;
import be.zwoop.repository.currency.CurrencyRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.post_status.PostStatusRepository;
import be.zwoop.repository.poststate.PostStateEntity;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.tag.TagRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.web.post.dto.PostDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static be.zwoop.domain.enum_type.PostStatusEnum.POST_INIT;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@AllArgsConstructor
@Component
public class PostFactory {

    private final PostStatusRepository postStatusRepository;
    private final CurrencyRepository currencyRepository;
    private final TagRepository tagRepository;

    public PostStateEntity buildInitPostState(PostEntity postEntity) {
        PostStatusEntity postStatus = postStatusRepository.findByPostStatusId(POST_INIT.getValue());
        return PostStateEntity.builder()
                .post(postEntity)
                .postStatus(postStatus)
                .build();
    }

    public PostEntity buildPostFromDto(PostDto postDto, UserEntity opEntity) {
        Optional<CurrencyEntity> currencyEntityOpt = currencyRepository.findByCurrencyCode(postDto.getCurrencyCode());

        if (currencyEntityOpt.isEmpty())
            throw new ResponseStatusException(BAD_REQUEST, "Currency not found");

        CurrencyEntity currencyEntity = currencyEntityOpt.get();

        List<TagEntity> tagEntities = tagRepository.findAllByTagIdIn(
                collectTagIdsByPostDto(postDto));

        return PostEntity.builder()
                .op(opEntity)
                .postTitle(postDto.getTitle())
                .postText(postDto.getText())
                .bidPrice(postDto.getBidPrice())
                .currency(currencyEntity)
                .tags(tagEntities)
                .build();
    }

    public List<Long> collectTagIdsByPostDto(PostDto postDto) {
        return postDto.getTags()
                .stream()
                .map(TagDto::getTagId)
                .collect(toList());
    }

}
