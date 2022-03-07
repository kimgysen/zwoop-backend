package be.zwoop.web.tag;


import be.zwoop.domain.model.tag.TagDto;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/public/tag")
public class TagControllerPublicV1 {

    private final TagRepository tagRepository;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagDto>> getTagsStartingWith(@RequestParam(value = "tagName") String tagName) {

        List<TagEntity> tagEntities = tagRepository.findByTagNameLike(tagName);

        return ok(TagDto.fromTagList(tagEntities));

    }

}
