package be.zwoop.amqp.domain.notification;

public enum NotificationType {
    DEAL_INIT,
    DEAL_CANCELLED,
    DEAL_PAID,

    ANSWER_ADDED,
    ANSWER_CHANGED,
    ANSWER_REMOVED
}
