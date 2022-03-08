package be.zwoop.service.post.notification;


import be.zwoop.amqp.topic.post_notification.PostNotificationSender;
import be.zwoop.amqp.topic.post_notification.mapper.PostUpdateDtoMapper;
import be.zwoop.repository.post.PostEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class PostNotificationServiceImpl implements PostNotificationService {
    private final PostUpdateDtoMapper postUpdateDtoMapper;
    private final PostNotificationSender postNotificationSender;


    @Override
    public void sendPostChangedNotification(PostEntity toUpdate) {
        try {
            postNotificationSender.sendPostUpdateNotification(
                    postUpdateDtoMapper.mapEntityToTopicDto(
                                    toUpdate.getPostId(), toUpdate));
        } catch(Exception e) {
            log.error("Error sending queue update for post: " + toUpdate.getPostId()
                    + " with error: " + e.getMessage());
        }
    }

}
