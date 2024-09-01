package io.github.xpakx.images.image;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.cache.annotation.CacheDecrement;
import io.github.xpakx.images.cache.annotation.CacheDelta;
import io.github.xpakx.images.comment.CommentService;
import io.github.xpakx.images.common.types.ResourceResult;
import io.github.xpakx.images.common.types.Result;
import io.github.xpakx.images.image.dto.ImageData;
import io.github.xpakx.images.image.dto.ImageDetails;
import io.github.xpakx.images.image.dto.UpdateImageRequest;
import io.github.xpakx.images.image.error.*;
import io.github.xpakx.images.like.LikeService;
import io.github.xpakx.images.upload.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
    private final LikeService likeService;
    private final CommentService commentService;
    private final UploadService uploadService;

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

    @CacheDelta(value = "postCountCache", key = "#username", delta = "#return.size()")
    public List<ImageData> uploadImages(MultipartFile[] files, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        List<Result<String>> results = Arrays
                .stream(files)
                .map(uploadService::trySave)
                .toList();
        System.out.println(results);
        return imageRepository.saveAll(
                results
                        .stream()
                        .filter(Result::isOk)
                        .map(Result::unwrap)
                        .map((image) -> toImageEntity(image, user.getId()))
                        .toList()
        ).stream()
                .map((img) -> imageToDto(img, username))
                .toList();
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
        image.setImageUrl(name);
        image.setUser(userRepository.getReferenceById(userId));
        return image;
    }

    public ResourceResult getImage(String sqId) {
        Long id = transformToId(sqId);

        String url = imageRepository
                .findById(id)
                .map(Image::getImageUrl)
                .orElseThrow(() -> new ImageNotFoundException("No image with such id"));
        return uploadService.getFile(url);
    }

    @CacheDecrement(value = "postCountCache", key = "#username")
    public void deleteImage(String imageId, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Long id = transformToId(imageId);
        var image = imageRepository
                .findById(id)
                .orElseThrow(() -> new ImageNotFoundException("No image with such id"));

        if(!user.getUsername().equals(image.getUser().getUsername())) {
            throw new NotAnOwnerException("Not an owner");
        }

        imageRepository.deleteById(id);
        uploadService.delete(image.getImageUrl());
    }

    private Long transformToId(String imageId) {
        List<Long> ids = sqids.decode(imageId);

        if(ids.size() != 1) {
            throw new IdCorruptedException("Id corrupted");
        }
        return ids.getFirst();
    }

    public ImageDetails getImageDetailsBySqId(String sqId, String username) {
        Long id = transformToId(sqId);
        var image = imageRepository
                .findById(id)
                .map(this::imageToDto)
                .orElseThrow(() -> new ImageNotFoundException("No image with such id"));
        long likes = likeService.getLikeCount(sqId);
        long comments = commentService.getCommentCount(sqId);
        return new ImageDetails(
                image.id(),
                image.caption(),
                image.createdAt(),
                image.author(),
                likes,
                comments,
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
        return likeService.likeExists(userId, imageId);
    }

    public ImageData updateImage(UpdateImageRequest request, String sqId, String username) {
        Long imageId = transformToId(sqId);
        var image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException("No image with such id"));

        if(!image.getUser().getUsername().equals(username)) {
            throw new NotAnOwnerException("Not an owner");
        }

        image.setCaption(request.caption());
        var result = imageRepository.save(image);
        return new ImageData(
                sqId,
                result.getCaption(),
                result.getCreatedAt(),
                username
        );
    }

    @Cacheable(value = "postCountCache", key = "#username")
    public long getPostCount(Long userId, String username) {
        return imageRepository.countByUserId(userId);
    }
}
