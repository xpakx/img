package io.github.xpakx.images.avatar;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.common.types.ResourceResult;
import io.github.xpakx.images.common.types.Result;
import io.github.xpakx.images.image.Image;
import io.github.xpakx.images.image.ImageRepository;
import io.github.xpakx.images.image.dto.ImageData;
import io.github.xpakx.images.image.dto.ImageDetails;
import io.github.xpakx.images.image.error.*;
import io.github.xpakx.images.like.LikeRepository;
import io.github.xpakx.images.profile.ProfileRepository;
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
public class AvatarService {
    private final ProfileRepository profileRepository;

    public void uploadAvatar(MultipartFile file, String username) {
        var user = profileRepository.findByUserUsername(username)
                .orElseThrow(UserNotFoundException::new);
        // TODO: better file names, saving url in entity
        Path root = Path.of("uploads/avatars");
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            Files.copy(file.getInputStream(), root.resolve(username));
        } catch (Exception e) {
            throw new CouldNotStoreException("Could not store the file");
        }
        user.setAvatar(true);
    }

    public ResourceResult getAvatar(String username) {
        Path root = Path.of("uploads/avatars");
        Path path = root.resolve(username);
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
}
