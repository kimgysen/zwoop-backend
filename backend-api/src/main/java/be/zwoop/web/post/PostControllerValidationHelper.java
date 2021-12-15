package be.zwoop.web.post;

import be.zwoop.domain.enum_type.PostFeedTypeEnum;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.tag.TagRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.web.exception.RequestParamException;
import be.zwoop.web.post.dto.ValidFeedParamDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static be.zwoop.domain.enum_type.PostFeedTypeEnum.*;

@Component
public class PostControllerValidationHelper {

    private UserRepository userRepository;
    private TagRepository tagRepository;


    ValidFeedParamDto validateTagId(Optional<Long> tagIdOpt) throws RequestParamException {
        ValidFeedParamDto validDto = new ValidFeedParamDto(FEED_BY_TAG);

        if (tagIdOpt.isEmpty()) {
            throw new RequestParamException("Query parameter 'tagId' is missing for type 'FEED_BY_TAG'.");
        }

        Optional<TagEntity> tagEntityOpt = tagRepository.findById(tagIdOpt.get());
        if (tagEntityOpt.isEmpty()) {
            throw new RequestParamException("Query parameter 'tagId' contains an invalid id.");

        } else {
            validDto.setTagEntity(tagEntityOpt.get());

        }

        return validDto;
    }

    ValidFeedParamDto validateTagIdList(Optional<List<Long>> tagIdListOpt) throws RequestParamException {
        ValidFeedParamDto validDto = new ValidFeedParamDto(FEED_BY_TAGS_LIST);

        if (tagIdListOpt.isEmpty()) {
            throw new RequestParamException("Query parameter 'tagId' is missing for type 'FEED_BY_TAG'.");

        } else if (tagIdListOpt.get().isEmpty()) {
            throw new RequestParamException("Query parameter 'tags' list should not be empty 'FEED_BY_TAGS'.");

        } else {
            List<TagEntity> tagEntities = tagRepository.findAllByTagIdIn(tagIdListOpt.get());
            validDto.setTagsEntityList(tagEntities);
        }

        return validDto;
    }

}
