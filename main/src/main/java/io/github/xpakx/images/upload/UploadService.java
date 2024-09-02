package io.github.xpakx.images.upload;

import io.github.xpakx.images.common.types.ResourceResult;
import io.github.xpakx.images.common.types.Result;
import io.github.xpakx.images.image.error.CannotLoadFileException;
import io.github.xpakx.images.image.error.CouldNotStoreException;
import io.github.xpakx.images.image.error.EmptyFilenameException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UploadService {
    Path root = Path.of("uploads");
    Path avatarRoot = root.resolve("avatars");
    private static final List<String> acceptedContentTypes = Arrays.asList("image/png", "image/jpeg");

    public Result<String> trySave(MultipartFile file) {
        return trySave(file, root);
    }

    public Result<String> trySaveAvatar(MultipartFile file) {
        return trySave(file, avatarRoot);
    }

    private Result<String> trySave(MultipartFile file, Path root) {
        if(Objects.isNull(file.getOriginalFilename()) || file.getOriginalFilename().isEmpty()) {
            return new Result.Err<>(new EmptyFilenameException("Filename cannot be empty!"));
        }

        String fileContentType = file.getContentType();
        if(fileContentType == null || !acceptedContentTypes.contains(fileContentType)) {
            return new Result.Err<>(new RuntimeException("Wrong filetype. Must be png or jpg."));
        }

        String type = switch (fileContentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            default -> ""; // unreachable
        };

        String checksum = "";
        try (InputStream data = file.getInputStream()) {
            byte[] hash = MessageDigest.getInstance("MD5").digest(data.readAllBytes());
            checksum = new BigInteger(1, hash).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return new Result.Err<>(new RuntimeException("Internal error."));
        } catch (IOException e) {
            return new Result.Err<>(new RuntimeException("Could not hash."));
        }
        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli();
        String name = String.format("%s%d.%s", checksum, timeStampMillis, type);

        // TODO: better file structure?

        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            Files.deleteIfExists(root.resolve(name));
            Files.copy(file.getInputStream(), root.resolve(name));
        } catch (Exception e) {
            return new Result.Err<>(new CouldNotStoreException("Could not store the file"));
        }
        return new Result.Ok<>(name);
    }

    public void delete(String url) {
        if ("avatars/default.jpg".equals(url)) {
            return;
        }
        try {
            Files.deleteIfExists(root.resolve(url));
        } catch (IOException e) {
            throw new RuntimeException("Cannot delete.");
        }
    }

    public ResourceResult getFile(String url) {
        Path path = root.resolve(url);
        if(Files.notExists(path)) {
            path = root.resolve("avatars/default.jpg"); // TODO
        }
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
