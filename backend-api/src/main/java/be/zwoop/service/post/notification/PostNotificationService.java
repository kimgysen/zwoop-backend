package be.zwoop.service.post.notification;

import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.web.post.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PostNotificationService {
    void sendPostChangedNotification(PostEntity postEntity);
}
