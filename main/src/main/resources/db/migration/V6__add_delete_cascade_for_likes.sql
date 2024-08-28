ALTER TABLE user_likes DROP CONSTRAINT fk_likes_on_image;
ALTER TABLE user_likes ADD CONSTRAINT fk_likes_on_image FOREIGN KEY (image_id) REFERENCES image (id) ON DELETE CASCADE;
