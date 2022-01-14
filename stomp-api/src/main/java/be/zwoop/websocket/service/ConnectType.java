package be.zwoop.websocket.service;

/**
 * This type provides some information on where in the webapp
 *  the user is connected.
 *  Depending on the connection type,
 *  we require some extra parameters to store them in the websocket session.
 *  GENERAL_APP subscribes to:
 *  - Private messages
 *  - Notifications
 */
public enum ConnectType {
    GENERAL_APP,
    PUBLIC_CHAT,
    PRIVATE_CHAT,
    POST_INBOX
}
