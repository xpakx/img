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
    private final UserRepository userRepository;
    private final UploadService uploadService;

    public void uploadAvatar(MultipartFile file, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        var name = uploadService.trySaveAvatar(file);
        if (!name.isOk()) {
            throw new RuntimeException("");
        }
        user.setAvatarUrl("avatars/" + name.unwrap());
        userRepository.save(user);
    }

    public ResourceResult getAvatar(String username) {
        return uploadService.getFile("avatars/" + username);
    }

    public void deleteAvatar(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        uploadService.delete(user.getAvatarUrl());
        user.setAvatarUrl("avatars/default.jpg");
        userRepository.save(user);
    }
}
