package io.github.xpakx.images.image;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.common.types.ResourceResult;
import io.github.xpakx.images.common.types.Result;
import io.github.xpakx.images.image.dto.ImageData;
import io.github.xpakx.images.image.dto.ImageDetails;
import io.github.xpakx.images.image.error.*;
import io.github.xpakx.images.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.sqids.Sqids;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final Sqids sqids;
    private final CacheManager cacheManager;
    private final LikeRepository likeRepository;

    public ImageData getBySqId(String sqId) {
        Long id = transformToId(sqId);
        return imageRepository
                .findById(id)
                .map(this::imageToDto)
                .orElseThrow(() -> new ImageNotFoundException("No image with such id"));
    }

    private ImageData imageToDto(Image image) {
        String id = sqids.encode(Collections.singletonList(image.getId()));
        return new ImageData(
                id,
                image.getCaption(),
                image.getCreatedAt(),
                image.getUser().getUsername()
        );
    }

    public Page<ImageData> getImagePage(String username, int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return imageRepository
                .findByUserUsername(username, pageable)
                .map(this::imageToDto);
    }

    public List<ImageData> uploadImages(MultipartFile[] files, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        List<Result<String>> results = Arrays
                .stream(files)
                .map(this::trySave)
                .toList();
        System.out.println(results);
        var result =  imageRepository.saveAll(
                results
                        .stream()
                        .filter(Result::isOk)
                        .map(Result::unwrap)
                        .map((image) -> toImageEntity(image, user.getId()))
                        .toList()
        ).stream()
                .map((img) -> imageToDto(img, username))
                .toList();

        updatePostCountCache(user.getId(), result.size());
        return result;
    }

    private ImageData imageToDto(Image image, String username) {
        String id = sqids.encode(Collections.singletonList(image.getId()));
        return new ImageData(
                id,
                image.getCaption(),
                image.getCreatedAt(),
                username
        );
    }

    private Image toImageEntity(String name, Long userId) {
        Image image = new Image();
        //TODO: make image private before editing caption etc.?
        image.setImageUrl("uploads/" + name);
        image.setUser(userRepository.getReferenceById(userId));
        return image;
    }

    private Result<String> trySave(MultipartFile file) {
        if(Objects.isNull(file.getOriginalFilename()) || file.getOriginalFilename().isEmpty()) {
            return new Result.Err<>(new EmptyFilenameException("Filename cannot be empty!"));
        }
        // TODO: better file structure and check mimetype

        Path root = Path.of("uploads");
        String name = file.getOriginalFilename();
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            Files.copy(file.getInputStream(), root.resolve(name));
        } catch (Exception e) {
            return new Result.Err<>(new CouldNotStoreException("Could not store the file"));
        }
        return new Result.Ok<>(name);
    }

    public ResourceResult getImage(String sqId) {
        Long id = transformToId(sqId);

        String url = imageRepository
                .findById(id)
                .map(Image::getImageUrl)
                .orElseThrow(() -> new ImageNotFoundException("No image with such id"));

        Path path = Paths.get(url);
        try {
            Resource resource = new UrlResource(path.toUri());
            String typeString = Files.probeContentType(path);
            MediaType type = switch (typeString) {
                case "image/jpeg" -> MediaType.IMAGE_JPEG;
                case "image/png" -> MediaType.IMAGE_PNG;
                default -> throw  new CannotLoadFileException("Incorrect filetype");
            };
            return new ResourceResult(resource, type);
        } catch (IOException e) {
            throw new CannotLoadFileException("Cannot load file");
        }
    }

    public void deleteImage(String imageId, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Long id = transformToId(imageId);
        String imageOwner = imageRepository
                .findById(id)
                .map((img) -> img.getUser().getUsername())
                .orElseThrow(() -> new ImageNotFoundException("No image with such id"));

        if(!user.getUsername().equals(imageOwner)) {
            throw new NotAnOwnerException("Not an owner");
        }

        imageRepository.deleteById(id);
        updatePostCountCache(user.getId(), -1);
    }

    private Long transformToId(String imageId) {
        List<Long> ids = sqids.decode(imageId);

        if(ids.size() != 1) {
            throw new IdCorruptedException("Id corrupted");
        }
        return ids.getFirst();
    }

    private void updatePostCountCache(Long userId, int delta) {
        var cache = cacheManager.getCache("postCountCache");
        if (cache != null) {
            Long currentLikeCount = cache.get(userId, Long.class);
            cache.put(
                    userId,
                    Objects.requireNonNullElseGet(
                            currentLikeCount,
                            () -> imageRepository.countByUserId(userId)
                    ) + delta
            );
        }
    }

    public ImageDetails getImageDetailsBySqId(String sqId, String username) {
        Long id = transformToId(sqId);
        var image = imageRepository
                .findById(id)
                .map(this::imageToDto)
                .orElseThrow(() -> new ImageNotFoundException("No image with such id"));
        long likes = likeRepository.countByImageId(id);
        return new ImageDetails(
                image.id(),
                image.caption(),
                image.createdAt(),
                image.author(),
                likes,
                checkLike(username, id),
                image.author().equals(username)
        );
    }

    private boolean checkLike(String username, Long imageId) {
        if (username == null) {
            return false;
        }
        var userId = userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);
        return likeRepository.existsByUserIdAndImageId(userId, imageId);
    }
}
