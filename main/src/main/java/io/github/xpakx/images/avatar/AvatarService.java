package io.github.xpakx.images.avatar;

import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.common.types.ResourceResult;
import io.github.xpakx.images.image.error.*;
import io.github.xpakx.images.profile.Profile;
import io.github.xpakx.images.profile.ProfileRepository;
import io.github.xpakx.images.upload.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UploadService uploadService;

    public void uploadAvatar(MultipartFile file, String username) {
        var user = profileRepository.findByUserUsername(username)
                .orElseGet(() -> createProfile(username));
        var name = uploadService.trySaveAvatar(file);
        user.setAvatarUrl("avatars/" + name);
        profileRepository.save(user);
    }

    private Profile createProfile(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Profile profile = new Profile();
        profile.setAvatarUrl("avatars/default.jpg");
        profile.setDescription("");
        profile.setUser(userRepository.getReferenceById(user.getId()));
        return profile;
    }

    public ResourceResult getAvatar(String username) {
        Path root = Path.of("uploads/avatars");
        Path path = root.resolve(username);
        if(Files.notExists(path)) {
            path = root.resolve("default.jpg");
        }
        try {
            Resource resource = new UrlResource(path.toUri());
            return new ResourceResult(resource, MediaType.IMAGE_JPEG);
        } catch (IOException e) {
            throw new CannotLoadFileException("Cannot load file");
        }
    }

    public void deleteAvatar(String username) {
        var user = profileRepository.findByUserUsername(username)
                .orElseThrow(UserNotFoundException::new);
        uploadService.delete(user.getAvatarUrl());
        user.setAvatarUrl("avatars/default.jpg");
        profileRepository.save(user);
    }
}
