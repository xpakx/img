package io.github.xpakx.images.comment;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.cache.annotation.CacheDecrement;
import io.github.xpakx.images.cache.annotation.CacheIncrement;
import io.github.xpakx.images.comment.dto.CommentData;
import io.github.xpakx.images.comment.dto.CommentRequest;
import io.github.xpakx.images.comment.error.CommentNotFoundException;
import io.github.xpakx.images.image.ImageRepository;
import io.github.xpakx.images.image.error.IdCorruptedException;
import io.github.xpakx.images.image.error.NotAnOwnerException;
import io.github.xpakx.images.image.error.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.sqids.Sqids;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final Sqids sqids;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @CacheIncrement(value = "commentCountCache", key = "#imageSqId")
    public CommentData addComment(CommentRequest request, String imageSqId, String username) {
        Long imageId = transformToId(imageSqId);
        Long userId = userRepository
                .findByUsername(username)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);
        Comment comment = new Comment();
        comment.setAuthor(userRepository.getReferenceById(userId));
        comment.setImage(imageRepository.getReferenceById(imageId));
        comment.setContent(request.content());
        var result = commentRepository.save(comment);
        return commentToDto(result, username, username);
    }

    private Long transformToId(String imageId) {
        List<Long> ids = sqids.decode(imageId);

        if(ids.size() != 1) {
            throw new IdCorruptedException("Id corrupted");
        }
        return ids.getFirst();
    }

    @CacheDecrement(value = "commentCountCache", key = "#return")
    public String deleteComment(Long commentId, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        var comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("No comment with such id"));

        if(!user.getUsername().equals(comment.getAuthor().getUsername())) {
            throw new NotAnOwnerException("Not an owner");
        }

        commentRepository.deleteById(commentId);
        return sqids.encode(List.of(comment.getImage().getId()));
    }

    private CommentData commentToDto(Comment comment, String username) {
        return new CommentData(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getUsername(),
                comment.getAuthor().getUsername().equals(username)
        );
    }

    private CommentData commentToDto(Comment comment, String author, String username) {
        return new CommentData(
                comment.getId(),
                comment.getContent(),
                author,
                author.equals(username)
        );
    }

    @Cacheable(value = "commentCountCache", key = "#imageSqId")
    public long getCommentCount(String imageSqId) {
        Long imageId = transformToId(imageSqId);
        return commentRepository.countByImageId(imageId);
    }

    public Page<CommentData> getCommentPage(String imageSqId, int page, String username) {
        Long imageId = transformToId(imageSqId);
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentRepository
                .findByImageId(imageId, pageable)
                .map((c) -> commentToDto(c, username));
    }
}
