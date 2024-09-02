ALTER TABLE user_likes
    ADD CONSTRAINT unique_like UNIQUE (user_id, image_id);
ALTER TABLE user_follows
    ADD CONSTRAINT unique_follow UNIQUE (user_id, follower_id);
