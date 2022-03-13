
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS  citext;

-- Create User table
CREATE TABLE IF NOT EXISTS "User"(
    user_id UUID NOT NULL DEFAULT gen_random_uuid(),
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email CITEXT UNIQUE,
    avatar TEXT,
    nick_name TEXT UNIQUE NOT NULL,
    about_text TEXT,
    is_blocked BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (user_id)
);

-- Create Role table
CREATE TABLE IF NOT EXISTS "Role"(
    role_id INT NOT NULL,
    role TEXT NOT NULL,

    PRIMARY KEY (role_id)
);

-- Create Use_Role table
CREATE TABLE IF NOT EXISTS "User_Role"(
    user_id UUID NOT NULL,
    role_id INT NOT NULL DEFAULT 1,

    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES "User"(user_id),
    FOREIGN KEY (role_id) REFERENCES "Role"(role_id)
);

-- Create AuthProvider table
CREATE TABLE IF NOT EXISTS "AuthProvider"(
    auth_provider_id INT NOT NULL,
    auth_provider TEXT NOT NULL,

    PRIMARY KEY (auth_provider_id)
);

-- CREATE User_AuthProvider table
CREATE TABLE IF NOT EXISTS "User_AuthProvider"(
    user_provider_id UUID NOT NULL,
    user_id UUID NOT NULL,
    auth_provider_id INT NOT NULL,
    oauth_user_id TEXT NOT NULL,

    PRIMARY KEY (user_provider_id),
    UNIQUE(user_id, auth_provider_id),
    UNIQUE(auth_provider_id, user_provider_id),
    FOREIGN KEY (user_id) REFERENCES "User"(user_id),
    FOREIGN KEY (auth_provider_id) REFERENCES "AuthProvider"(auth_provider_id)
);

-- Create Currency table
CREATE TABLE IF NOT EXISTS "Currency"(
    currency_id INT NOT NULL,
    currency_code TEXT NOT NULL,

    PRIMARY KEY (currency_id)
);

-- Create Post table
CREATE TABLE IF NOT EXISTS "Post"(
    post_id UUID NOT NULL DEFAULT gen_random_uuid(),
    original_poster_id UUID NOT NULL,
    post_title TEXT NOT NULL,
    post_text TEXT NOT NULL,
    bid_price REAL,
    currency_id INT,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (post_id),
    UNIQUE(original_poster_id, post_title),
    FOREIGN KEY(original_poster_id) REFERENCES "User"(user_id),
    FOREIGN KEY(currency_id) REFERENCES "Currency"(currency_id)
);

-- Create Tag table
CREATE TABLE IF NOT EXISTS "Tag"(
   tag_id SERIAL NOT NULL,
   tag TEXT NOT NULL UNIQUE,
   description TEXT,

   PRIMARY KEY (tag_id)
);

-- Create Post_Tag table
CREATE TABLE IF NOT EXISTS "Post_Tag"(
    post_id UUID NOT NULL,
    tag_id INT NOT NULL,

    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY(post_id) REFERENCES "Post"(post_id),
    FOREIGN KEY(tag_id) REFERENCES "Tag"(tag_id)
);

-- Create User_Tag table
CREATE TABLE IF NOT EXISTS "User_Tag"(
    user_id UUID NOT NULL,
    tag_id INT NOT NULL,

    PRIMARY KEY (user_id, tag_id),
    FOREIGN KEY (user_id) REFERENCES "User"(user_id),
    FOREIGN KEY (tag_id) REFERENCES "Tag"(tag_id)
);

-- Create Bidding table
CREATE TABLE IF NOT EXISTS "Bidding"(
    bidding_id UUID NOT NULL DEFAULT gen_random_uuid(),
    post_id UUID NOT NULL,
    consultant_id UUID NOT NULL,
    ask_price REAL NOT NULL,
    currency_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (bidding_id),
    UNIQUE(post_id, consultant_id),
    FOREIGN KEY (post_id) REFERENCES "Post"(post_id),
    FOREIGN KEY (consultant_id) REFERENCES "User"(user_id),
    FOREIGN KEY (currency_id) REFERENCES "Currency"(currency_id)
);

-- Create Deal table
CREATE TABLE IF NOT EXISTS "Deal"(
    deal_id UUID NOT NULL DEFAULT gen_random_uuid(),
    bidding_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (deal_id),
    FOREIGN KEY (bidding_id) REFERENCES "Bidding"(bidding_id)
);

-- Create Answer table
CREATE TABLE IF NOT EXISTS "Answer" (
    answer_id UUID NOT NULL DEFAULT gen_random_uuid(),
    post_id UUID NOT NULL,
    consultant_id UUID NOT NULL,
    answer_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (answer_id),
    UNIQUE (post_id, consultant_id),
    FOREIGN KEY (post_id) REFERENCES "Post"(post_id),
    FOREIGN KEY (consultant_id) REFERENCES "User"(user_id)
);

-- Create Review table
CREATE TABLE IF NOT EXISTS "Review" (
    review_id UUID NOT NULL DEFAULT gen_random_uuid(),
    post_id UUID NOT NULL,
    op_id UUID NOT NULL,
    consultant_id UUID NOT NULL,
    review_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (review_id),
    UNIQUE(post_id, op_id, consultant_id),
    FOREIGN KEY (post_id) REFERENCES "Post"(post_id),
    FOREIGN KEY (op_id) REFERENCES "User"(user_id),
    FOREIGN KEY (consultant_id) REFERENCES "User"(user_id)
);

-- Create PostStatus table
CREATE TABLE IF NOT EXISTS "PostStatus"(
    post_status_id INT NOT NULL,
    status TEXT NOT NULL,
    description TEXT,
    PRIMARY KEY (post_status_id)
);

-- Create PostState table
CREATE TABLE IF NOT EXISTS "PostState" (
    post_state_id UUID NOT NULL DEFAULT gen_random_uuid(),
    post_id UUID NOT NULL UNIQUE,
    post_status_id INT NOT NULL DEFAULT 1,
    deal_id UUID,
    answer_id UUID,
    review_id UUID,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (post_state_id),

    FOREIGN KEY (post_id) REFERENCES "Post"(post_id),
    FOREIGN KEY (post_status_id) REFERENCES "PostStatus"(post_status_id),
    FOREIGN KEY (deal_id) REFERENCES "Deal"(deal_id),
    FOREIGN KEY (answer_id) REFERENCES "Answer"(answer_id),
    FOREIGN KEY (review_id) REFERENCES "Review"(review_id)
);

-- Create NotificationType table
CREATE TABLE IF NOT EXISTS "NotificationType"(
    notification_type_id INT NOT NULL,
    notification_type TEXT NOT NULL,
    description TEXT NOT NULL,

    PRIMARY KEY (notification_type_id)
);

CREATE TABLE IF NOT EXISTS "UserNotificationCount"(
    user_id UUID NOT NULL,
    unread_count INT DEFAULT 0,

    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES "User"(user_id)
);

CREATE TABLE IF NOT EXISTS "UserNotification"(
    user_notification_id UUID NOT NULL,
    sender_id UUID NULL,
    receiver_id UUID NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    redirect_param TEXT,
    meta_info TEXT,
    notification_type_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL,

    PRIMARY KEY (user_notification_id),
    FOREIGN KEY (sender_id) REFERENCES "User"(user_id),
    FOREIGN KEY (receiver_id) REFERENCES "User"(user_id),
    FOREIGN KEY (notification_type_id) REFERENCES "NotificationType"(notification_type_id)
);
