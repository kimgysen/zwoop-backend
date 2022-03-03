package be.zwoop.amqp.domain.post_update;

public enum PostUpdateType {
    POST_CHANGED,
    POST_REMOVED,
    BIDDING_ADDED,
    BIDDING_REMOVED,
    BIDDING_CHANGED,
    DEAL_INIT,
    DEAL_CANCELLED,
    ANSWER_ADDED,
    ANSWER_CHANGED,
    ANSWER_REMOVED
}
