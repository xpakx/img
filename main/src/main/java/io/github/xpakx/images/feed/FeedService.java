package io.github.xpakx.images.feed;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.image.Image;
import io.github.xpakx.images.image.ImageRepository;
import io.github.xpakx.images.image.dto.ImageData;
import io.github.xpakx.images.image.error.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.sqids.Sqids;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final Sqids sqids;

    public Page<ImageData> getLiked(String username, int page) {
        Long userId = userRepository
                .findByUsername(username)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);

        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return imageRepository
                .getLiked(userId, pageable)
                .map(this::imageToDto);
    }

    private ImageData imageToDto(Image image) {
        String id = sqids.encode(Collections.singletonList(image.getId()));
        return new ImageData(
                id,
                image.getCaption(),
                image.getImageUrl(),
                image.getCreatedAt(),
                image.getUser().getUsername()
        );
    }

    public Page<ImageData> getFollowFeed(String username, int page) {
        Long userId = userRepository
                .findByUsername(username)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);

        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return imageRepository
                .getFollowFeed(userId, pageable)
                .map(this::imageToDto);
    }
}
