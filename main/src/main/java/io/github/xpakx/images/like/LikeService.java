package io.github.xpakx.images.like;

import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.image.ImageRepository;
import io.github.xpakx.images.image.error.IdCorruptedException;
import io.github.xpakx.images.image.error.ImageNotFoundException;
import io.github.xpakx.images.image.error.UserNotFoundException;
import io.github.xpakx.images.like.dto.ImageLikes;
import io.github.xpakx.images.like.error.ImageOwnerException;
import io.github.xpakx.images.like.error.LikeExistsException;
import io.github.xpakx.images.like.error.LikeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.sqids.Sqids;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final Sqids sqids;
    private final CacheManager cacheManager;

    public void likeImage(String username, String imageSqId) {
        Long imageId = transformToId(imageSqId);
        var imageAuthor = imageRepository
                .findById(imageId)
                .map((img) -> img.getUser().getUsername())
                .orElseThrow(() -> new ImageNotFoundException("Image not found."));

        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (imageAuthor.equals(username)) {
            throw new ImageOwnerException("Cannot like own images.");
        }

        if (likeRepository.existsByUserIdAndImageId(user.getId(), imageId)) {
            throw new LikeExistsException("User has already liked this image.");
        }

        Like like = new Like();
        like.setUser(userRepository.getReferenceById(user.getId()));
        like.setImage(imageRepository.getReferenceById(imageId));

        likeRepository.save(like);
        updateLikeCountCache(imageId, 1);
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
                .orElseThrow(() -> new LikeNotFoundException("Like not found."));
        likeRepository.delete(like);
        updateLikeCountCache(imageId, -1);
    }

    public ImageLikes getLikeCount(String imageSqId) {
        Long imageId = transformToId(imageSqId);
        var likes = likeRepository.countByImageId(imageId);
        return new ImageLikes(likes);
    }

    private void updateLikeCountCache(Long imageId, int delta) {
        var cache = cacheManager.getCache("likeCountCache");
        if (cache != null) {
            Long currentLikeCount = cache.get(imageId, Long.class);
            cache.put(
                    imageId,
                    Objects.requireNonNullElseGet(
                            currentLikeCount,
                            () -> likeRepository.countByImageId(imageId)
                    ) + delta
            );
        }
    }
}
