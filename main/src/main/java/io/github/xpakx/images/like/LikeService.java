package io.github.xpakx.images.like;

import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.image.ImageRepository;
import io.github.xpakx.images.image.ImageService;
import io.github.xpakx.images.image.error.IdCorruptedException;
import io.github.xpakx.images.image.error.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sqids.Sqids;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final Sqids sqids;

    public void likeImage(String username, String imageSqId) {
        Long imageId = transformToId(imageSqId);
        var imageAuthor = imageRepository
                .findById(imageId)
                .map((img) -> img.getUser().getUsername())
                .orElseThrow();

        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (imageAuthor.equals(username)) {
            throw new RuntimeException("Cannot like own images.");
        }

        if (likeRepository.existsByUserIdAndImageId(user.getId(), imageId)) {
            throw new RuntimeException("User has already liked this image.");
        }

        Like like = new Like();
        like.setUser(userRepository.getReferenceById(user.getId()));
        like.setImage(imageRepository.getReferenceById(imageId));

        likeRepository.save(like);
    }

    private Long transformToId(String imageId) {
        List<Long> ids = sqids.decode(imageId);

        if(ids.size() != 1) {
            throw new IdCorruptedException("Id corrupted");
        }
        return ids.getFirst();
    }

    public void unlikeImage(String username, String imageSqId) {
        Long imageId = transformToId(imageSqId);
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Like like = likeRepository.findByUserIdAndImageId(user.getId(), imageId)
                .orElseThrow(() -> new RuntimeException("Like not found."));
        likeRepository.delete(like);
    }
}
