package be.zwoop.domain.notification.queue;

public enum UserNotificationType {
    BIDDING_ADDED,
    BIDDING_CHANGED,
    BIDDING_REMOVED,

    DEAL_INIT,
    DEAL_CANCELLED,

    ANSWER_ADDED,
    ANSWER_CHANGED,
    ANSWER_REMOVED,
    ANSWER_ACCEPTED,

    SMART_CONTRACT_PAID
}
