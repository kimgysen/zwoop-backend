
-- Populate configuration tables

-- Role
INSERT INTO "Role" (role_id, role) VALUES(1, 'ROLE_USER');
INSERT INTO "Role" (role_id, role) VALUES(2, 'ROLE_MODERATOR');
INSERT INTO "Role" (role_id, role) VALUES(3, 'ROLE_ADMIN');

-- AuthProvider
INSERT INTO "AuthProvider" (auth_provider_id, auth_provider) VALUES (1, 'GOOGLE');
INSERT INTO "AuthProvider" (auth_provider_id, auth_provider) VALUES (2, 'LINKEDIN');
INSERT INTO "AuthProvider" (auth_provider_id, auth_provider) VALUES (3, 'GITHUB');
INSERT INTO "AuthProvider" (auth_provider_id, auth_provider) VALUES (4, 'TWITTER');
INSERT INTO "AuthProvider" (auth_provider_id, auth_provider) VALUES (5, 'FACEBOOK');

-- Currency
INSERT INTO "Currency" (currency_id, currency_code) VALUES(1, 'BUSD');

-- PostStatus
INSERT INTO "PostStatus" (post_status_id, status, description) VALUES(1, 'POST_INIT', 'Post uploaded');
INSERT INTO "PostStatus" (post_status_id, status, description) VALUES(2, 'DEAL_INIT', 'Bidding accepted and deal initialized');
INSERT INTO "PostStatus" (post_status_id, status, description) VALUES(3, 'ANSWERED', 'Post answered');
INSERT INTO "PostStatus" (post_status_id, status, description) VALUES(4, 'ANSWER_ACCEPTED', 'Answer accepted');
INSERT INTO "PostStatus" (post_status_id, status, description) VALUES(5, 'PAID', 'Answer paid');
INSERT INTO "PostStatus" (post_status_id, status, description) VALUES(6, 'REVIEWED', 'Post review written by OP');

-- NotificationTpe
INSERT INTO "NotificationType" (notification_type_id, notification_type, description) VALUES ('1', 'BIDDING_ADDED', 'Consultant added a bidding for post');
INSERT INTO "NotificationType" (notification_type_id, notification_type, description) VALUES ('2', 'BIDDING_CHANGED', 'Consultant changed the bidding price');
INSERT INTO "NotificationType" (notification_type_id, notification_type, description) VALUES ('3', 'BIDDING_REMOVED', 'Consultant removed his / her bidding');
INSERT INTO "NotificationType" (notification_type_id, notification_type, description) VALUES ('4', 'DEAL_INIT', 'Bidding accepted by the OP');
INSERT INTO "NotificationType" (notification_type_id, notification_type, description) VALUES ('5', 'DEAL_CANCELLED', 'Deal cancelled by the OP');
INSERT INTO "NotificationType" (notification_type_id, notification_type, description) VALUES ('6', 'ANSWER_ADDED', 'Answer posted by the consultant');
INSERT INTO "NotificationType" (notification_type_id, notification_type, description) VALUES ('7', 'ANSWER_CHANGED', 'Answer changed by the consultant');
INSERT INTO "NotificationType" (notification_type_id, notification_type, description) VALUES ('8', 'ANSWER_REMOVED', 'Answer removed by the consultant');
INSERT INTO "NotificationType" (notification_type_id, notification_type, description) VALUES ('9', 'ANSWER_ACCEPTED', 'Answer was accepted by the OP');
INSERT INTO "NotificationType" (notification_type_id, notification_type, description) VALUES ('10', 'PAID', 'Smart contract transferred the payment to the consultant');
