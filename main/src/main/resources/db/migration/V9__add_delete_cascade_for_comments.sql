ALTER TABLE user_comments ADD CONSTRAINT fk_comment_on_image FOREIGN KEY (image_id) REFERENCES image (id) ON DELETE CASCADE;
