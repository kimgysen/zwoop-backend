package be.zwoop.service.post.factory;

import be.zwoop.domain.model.tag.TagDto;
import be.zwoop.repository.currency.CurrencyEntity;
import be.zwoop.repository.currency.CurrencyRepository;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.tag.TagRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.web.post.dto.SavePostDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@AllArgsConstructor
@Component
public class PostFactoryImpl implements PostFactory {
    private final CurrencyRepository currencyRepository;
    private final TagRepository tagRepository;

    public PostEntity buildPostFromDto(SavePostDto savePostDto, UserEntity opEntity) {
        Optional<CurrencyEntity> currencyEntityOpt = currencyRepository.findByCurrencyCode(savePostDto.getCurrencyCode());

        if (currencyEntityOpt.isEmpty())
            throw new ResponseStatusException(BAD_REQUEST, "Currency not found");

        CurrencyEntity currencyEntity = currencyEntityOpt.get();

        List<TagEntity> tagEntities = tagRepository.findAllByTagIdIn(
                collectTagIdsByPostDto(savePostDto));

        return PostEntity.builder()
                .op(opEntity)
                .postTitle(savePostDto.getTitle())
                .postText(savePostDto.getText())
                .bidPrice(savePostDto.getBidPrice())
                .currency(currencyEntity)
                .tags(tagEntities)
                .build();
    }

    public List<Long> collectTagIdsByPostDto(SavePostDto savePostDto) {
        return savePostDto.getTags()
                .stream()
                .map(TagDto::getTagId)
                .collect(toList());
    }

}
