package be.zwoop.domain.enum_type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationTypeEnum {
    BIDDING_ADDED(1),
    BIDDING_CHANGED(2),
    BIDDING_REMOVED(3),
    DEAL_INIT(4),
    DEAL_CANCELLED(5),
    ANSWER_ADDED(6),
    ANSWER_CHANGED(7),
    ANSWER_REMOVED(8),
    ANSWER_ACCEPTED(9),
    PAID(10);

    @Getter
    private final int value;

}
