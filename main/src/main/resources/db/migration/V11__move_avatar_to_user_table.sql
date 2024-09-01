ALTER TABLE profile
    DROP COLUMN avatar_url;
ALTER TABLE account
    ADD COLUMN avatar_url VARCHAR(255) NOT NULL DEFAULT 'avatars/default.jpg';
