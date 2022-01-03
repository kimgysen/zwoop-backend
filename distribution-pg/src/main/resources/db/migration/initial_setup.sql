
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS  citext;

-- Create User table
CREATE TABLE IF NOT EXISTS "User"(
    user_id UUID NOT NULL DEFAULT gen_random_uuid(),
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email CITEXT UNIQUE,
    profile_pic TEXT,
    nick_name TEXT UNIQUE,
    public_address_trx TEXT UNIQUE,
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

-- Create PostStatus table
CREATE TABLE IF NOT EXISTS "PostStatus"(
    post_status_id INT NOT NULL,
    post_status TEXT NOT NULL,

    PRIMARY KEY (post_status_id)
);

-- Create Currency table
CREATE TABLE IF NOT EXISTS "Currency"(
    currency_id INT NOT NULL,
    currency TEXT NOT NULL,

    PRIMARY KEY (currency_id)
);

-- Create Post table
CREATE TABLE IF NOT EXISTS "Post"(
    post_id UUID NOT NULL DEFAULT gen_random_uuid(),
    asker_id UUID NOT NULL,
    post_title TEXT NOT NULL,
    post_text TEXT NOT NULL,
    offer_price REAL,
    currency_id INT,
    post_status_id INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (post_id),
    UNIQUE(asker_id, post_title),
    FOREIGN KEY(asker_id) REFERENCES "User"(user_id),
    FOREIGN KEY(post_status_id) REFERENCES "PostStatus"(post_status_id),
    FOREIGN KEY(currency_id) REFERENCES "Currency"(currency_id)
);

-- Create Tag table
CREATE TABLE IF NOT EXISTS "Tag"(
   tag_id SERIAL NOT NULL,
   tag TEXT NOT NULL,
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

-- Create ApplicationStatus table
CREATE TABLE IF NOT EXISTS "ApplicationStatus"(
     application_status_id INT NOT NULL,
     application_status TEXT NOT NULL,

     PRIMARY KEY (application_status_id)
);

-- Create Application table
CREATE TABLE IF NOT EXISTS "Application"(
    application_id UUID NOT NULL DEFAULT gen_random_uuid(),
    post_id UUID NOT NULL,
    respondent_id UUID NOT NULL,
    application_text TEXT NOT NULL,
    ask_price REAL,
    application_status_id INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (application_id),
    UNIQUE(post_id, respondent_id),
    FOREIGN KEY (post_id) REFERENCES "Post"(post_id),
    FOREIGN KEY (respondent_id) REFERENCES "User"(user_id),
    FOREIGN KEY (application_status_id) REFERENCES "ApplicationStatus"(application_status_id)
);

-- Create AnswerStatus table
CREATE TABLE IF NOT EXISTS "AnswerStatus"(
    answer_status_id INT NOT NULL,
    answer_status TEXT NOT NULL,

    PRIMARY KEY (answer_status_id)
);

-- Create Answer table
CREATE TABLE IF NOT EXISTS "Answer" (
    answer_id UUID NOT NULL DEFAULT gen_random_uuid(),
    post_id UUID NOT NULL,
    respondent_id UUID NOT NULL,
    answer_text TEXT NOT NULL,
    answer_status_id INT NOT NULL DEFAULT 1,
    closing_price REAL,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (answer_id),
    UNIQUE (post_id, respondent_id),
    FOREIGN KEY (post_id) REFERENCES "Post"(post_id),
    FOREIGN KEY (respondent_id) REFERENCES "User"(user_id),
    FOREIGN KEY (answer_status_id) REFERENCES "AnswerStatus"(answer_status_id)
);

-- Create Review table
CREATE TABLE IF NOT EXISTS "Review" (
    review_id UUID NOT NULL DEFAULT gen_random_uuid(),
    post_id UUID NOT NULL,
    reviewer_id UUID NOT NULL,
    reviewee_id UUID NOT NULL,
    review_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (review_id),
    UNIQUE(post_id, reviewer_id, reviewee_id),
    FOREIGN KEY (post_id) REFERENCES "Post"(post_id),
    FOREIGN KEY (reviewer_id) REFERENCES "User"(user_id),
    FOREIGN KEY (reviewee_id) REFERENCES "User"(user_id)
);

-- Create NotificationType table
CREATE TABLE IF NOT EXISTS "NotificationType"(
   notification_type_id INT NOT NULL,
   notification_type TEXT NOT NULL,

   PRIMARY KEY (notification_type_id)
);

-- Create Notification table
CREATE TABLE IF NOT EXISTS "Notification"(
    notification_id UUID NOT NULL DEFAULT gen_random_uuid(),
    notification_type_id INT NOT NULL,
    dest_user_id UUID NOT NULL,

    PRIMARY KEY (notification_id),
    FOREIGN KEY (notification_type_id) REFERENCES "NotificationType"(notification_type_id),
    FOREIGN KEY (dest_user_id) REFERENCES "User"(user_id)
);

-- Create ChatMessage table
CREATE TABLE IF NOT EXISTS "ChatMessage"(
    message_id UUID NOT NULL DEFAULT gen_random_uuid(),
    src_user_id UUID NOT NULL,
    dest_user_id UUID NOT NULL ,
    message_text TEXT,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,

    PRIMARY KEY (message_id),
    FOREIGN KEY (src_user_id) REFERENCES "User"(user_id),
    FOREIGN KEY (dest_user_id) REFERENCES "User"(user_id)
);
