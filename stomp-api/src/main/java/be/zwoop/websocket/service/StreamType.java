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
public enum StreamType {
    GENERAL_APP,
    TAG_PUBLIC_CHAT,
    POST_PRIVATE_CHAT,
    POST_INBOX
}
