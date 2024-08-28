package io.github.xpakx.images.comment;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.comment.dto.CommentData;
import io.github.xpakx.images.comment.dto.CommentRequest;
import io.github.xpakx.images.comment.error.CommentNotFoundException;
import io.github.xpakx.images.image.ImageRepository;
import io.github.xpakx.images.image.dto.ImageData;
import io.github.xpakx.images.image.error.IdCorruptedException;
import io.github.xpakx.images.image.error.NotAnOwnerException;
import io.github.xpakx.images.image.error.UserNotFoundException;
import lombok.RequiredArgsConstructor;
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
        return commentToDto(result, username);
    }

    private Long transformToId(String imageId) {
        List<Long> ids = sqids.decode(imageId);

        if(ids.size() != 1) {
            throw new IdCorruptedException("Id corrupted");
        }
        return ids.getFirst();
    }


    public void deleteComment(Long commentId, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        String commentAuthor = commentRepository
                .findById(commentId)
                .map((img) -> img.getAuthor().getUsername())
                .orElseThrow(() -> new CommentNotFoundException("No comment with such id"));

        if(!user.getUsername().equals(commentAuthor)) {
            throw new NotAnOwnerException("Not an owner");
        }

        commentRepository.deleteById(commentId);
    }

    private CommentData commentToDto(Comment comment) {
        return new CommentData(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getUsername()
        );
    }

    private CommentData commentToDto(Comment comment, String username) {
        return new CommentData(
                comment.getId(),
                comment.getContent(),
                username
        );
    }

    public Page<CommentData> getCommentPage(String imageSqId, int page) {
        Long imageId = transformToId(imageSqId);
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentRepository
                .findByImageId(imageId, pageable)
                .map(this::commentToDto);
    }
}
