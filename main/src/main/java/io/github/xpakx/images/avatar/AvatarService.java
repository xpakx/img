package io.github.xpakx.images.avatar;

import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.common.types.ResourceResult;
import io.github.xpakx.images.image.error.*;
import io.github.xpakx.images.profile.Profile;
import io.github.xpakx.images.profile.ProfileRepository;
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

    public void uploadAvatar(MultipartFile file, String username) {
        var user = profileRepository.findByUserUsername(username)
                .orElseGet(() -> createProfile(username));
        // TODO: better file names, saving url in entity
        Path root = Path.of("uploads/avatars");
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            Files.deleteIfExists(root.resolve(username));
            Files.copy(file.getInputStream(), root.resolve(username));
        } catch (Exception e) {
            throw new CouldNotStoreException("Could not store the file");
        }
        user.setAvatar(true);
        profileRepository.save(user);
    }

    private Profile createProfile(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Profile profile = new Profile();
        profile.setAvatar(false);
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

        Path root = Path.of("uploads/avatars");
        Path path = root.resolve(username);

        try {
            boolean result = Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Cannot delete avatar.");
        }

        user.setAvatar(false);
        profileRepository.save(user);
    }
}
