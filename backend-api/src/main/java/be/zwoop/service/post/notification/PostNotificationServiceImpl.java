package be.zwoop.service.post.notification;


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
import be.zwoop.service.post.PostFactory;
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
public class PostNotificationServiceImpl implements PostNotificationService {
    private final PostUpdateDtoMapper postUpdateDtoMapper;
    private final PostNotificationSender postNotificationSender;


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

}
