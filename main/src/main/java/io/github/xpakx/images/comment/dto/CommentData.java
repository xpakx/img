package io.github.xpakx.images.comment.dto;

public record CommentData(Long id, String content, String author, String avatarUrl, boolean owner) {
}
