package be.zwoop.web.post;

import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.tag.TagRepository;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.web.exception.RequestParamException;
import be.zwoop.web.post.dto.ValidFeedParamDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static be.zwoop.domain.enum_type.PostFeedTypeEnum.FEED_BY_TAG;

@AllArgsConstructor
@Component
public class PostControllerValidationHelper {

    private final TagRepository tagRepository;


    ValidFeedParamDto validateTagName(Optional<String> tagNameOpt) throws RequestParamException {
        ValidFeedParamDto validDto = new ValidFeedParamDto(FEED_BY_TAG);

        if (tagNameOpt.isEmpty()) {
            throw new RequestParamException("Query parameter 'tagId' is missing for type 'FEED_BY_TAG'.");
        }

        Optional<TagEntity> tagEntityOpt = tagRepository.findByTagName(tagNameOpt.get());
        if (tagEntityOpt.isEmpty()) {
            throw new RequestParamException("Query parameter 'tagName' doesn\'t exist.");

        } else {
            validDto.setTagEntity(tagEntityOpt.get());

        }

        return validDto;
    }

}
