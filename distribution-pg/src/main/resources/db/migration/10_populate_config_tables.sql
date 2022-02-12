
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
INSERT INTO "Currency" (currency_id, currency_code) VALUES(1, 'BNB');

-- PostStatus
INSERT INTO "PostStatus" (post_status_id, post_status) VALUES(1, 'OPEN');
INSERT INTO "PostStatus" (post_status_id, post_status) VALUES(2, 'IN_PROGRESS');
INSERT INTO "PostStatus" (post_status_id, post_status) VALUES(3, 'CLOSED');

-- BiddingStatus
INSERT INTO "BiddingStatus" (bidding_status_id, bidding_status) VALUES(1, 'PENDING');
INSERT INTO "BiddingStatus" (bidding_status_id, bidding_status) VALUES(2, 'ACCEPTED');

-- AnswerStatus
INSERT INTO "AnswerStatus" (answer_status_id, answer_status) VALUES(1, 'PENDING');
INSERT INTO "AnswerStatus" (answer_status_id, answer_status) VALUES(2, 'ACCEPTED');

