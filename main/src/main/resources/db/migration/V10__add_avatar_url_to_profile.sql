ALTER TABLE profile
    DROP COLUMN avatar,
    ADD COLUMN avatar_url VARCHAR(255) NOT NULL DEFAULT 'avatars/default.jpg';
