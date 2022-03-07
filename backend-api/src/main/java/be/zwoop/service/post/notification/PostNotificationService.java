package be.zwoop.service.post.notification;

import be.zwoop.repository.post.PostEntity;

public interface PostNotificationService {
    void sendPostChangedNotification(PostEntity postEntity);
}
