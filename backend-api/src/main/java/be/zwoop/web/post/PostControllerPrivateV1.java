package be.zwoop.web.post;


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
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.web.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/post")
public class PostControllerPrivateV1 {

    private final AuthenticationFacade authenticationFacade;
    private final PostRepository postRepository;
    private final PostStatusRepository postStatusRepository;
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> savePost(@Valid @RequestBody PostDto postDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        // TODO: Optional check if active and not blocked
        Optional<UserEntity> askerEntityOpt = userRepository.findByUserId(principalId);

        if (askerEntityOpt.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Asker doesn\'t exist for UUID: " + principalId.toString());
        }

        UserEntity askerEntity = askerEntityOpt.get();
        Optional<PostEntity> postEntityOpt = postRepository.findByAskerAndPostTitle(askerEntity, postDto.getTitle());

        CurrencyEntity currencyEntity = null;
        if (postDto.getOffer() != null) {
            if (postDto.getCurrency() == null) {
                throw new ResponseStatusException(BAD_REQUEST, "Currency is null while offer price is defined.");
            } else {
                Optional<CurrencyEntity> currencyEntityOpt = currencyRepository.findByCurrency(postDto.getCurrency());
                if (currencyEntityOpt.isEmpty()) {
                    throw new ResponseStatusException(BAD_REQUEST, "Currency not found");
                } else {
                    currencyEntity = currencyEntityOpt.get();
                }
            }

        }

        if (postEntityOpt.isPresent()) {
            throw new ResponseStatusException(BAD_REQUEST, "Post already exists with id: " + postEntityOpt.get().getPostId());

        } else {
            List<TagEntity> tagEntities = tagRepository.findAllByTagIdIn(postDto.getTagIds());
            PostStatusEntity postStatusEntity = postStatusRepository
                    .findById(PostStatusEnum.OPEN.getValue())
                    .orElse(null);

            PostEntity toSave = PostEntity.builder()
                    .asker(askerEntity)
                    .postStatus(postStatusEntity)
                    .postTitle(postDto.getTitle())
                    .postText(postDto.getText())
                    .offerPrice(postDto.getOffer())
                    .currency(currencyEntity)
                    .postStatus(postStatusEntity)
                    .tags(tagEntities)
                    .build();
            PostEntity savedPost = postRepository.saveAndFlush(toSave);

            URI uri = UriComponentsBuilder
                    .fromPath(("/post/{id}"))
                    .buildAndExpand(savedPost.getPostId()).toUri();
            return created(uri).build();
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable UUID postId) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        // TODO: Optional check if active and not blocked
        UserEntity askerEntity = userRepository.getById(principalId);
        Optional<PostEntity> postEntityOpt = postRepository.findById(postId);

        if (postEntityOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND);

        } else {
            PostEntity toUpdate = postEntityOpt.get();

            toUpdate.setPostTitle(postDto.getTitle());
            toUpdate.setPostText(postDto.getText());
            List<TagEntity> tagEntities = tagRepository.findAllByTagIdIn(postDto.getTagIds());
            toUpdate.setTags(tagEntities);

            postRepository.saveAndFlush(toUpdate);

            return noContent().build();
        }
    }

    private void validateAsker(UserEntity askerEntity) {
        if (!askerEntity.isActive()) {
            throw new ResponseStatusException(BAD_REQUEST, "Asker is not active: " + askerEntity.getUserId().toString());
        }

        if (askerEntity.isBlocked()) {
            throw new ResponseStatusException(BAD_REQUEST, "Asker is blocked: " + askerEntity.getUserId().toString());
        }
    }

}
