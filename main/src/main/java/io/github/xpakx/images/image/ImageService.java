package io.github.xpakx.images.image;

import io.github.xpakx.images.common.types.Result;
import io.github.xpakx.images.image.dto.ImageData;
import io.github.xpakx.images.image.error.IdCorruptedException;
import io.github.xpakx.images.image.error.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.sqids.Sqids;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final Sqids sqids;

    public ImageData getBySqId(String sqid) {
        List<Long> ids = sqids.decode(sqid);

        if(ids.size() != 1) {
            throw new IdCorruptedException("Id corrupted");
        }
        return imageRepository
                .findById(ids.getFirst())
                .map(this::imageToDto)
                .orElseThrow(() -> new ImageNotFoundException("No image with such id"));
    }

    private ImageData imageToDto(Image image) {
        String id = sqids.encode(Collections.singletonList(image.getId()));
        return new ImageData(
                id,
                image.getImageUrl(),
                image.getCaption(),
                image.getCreatedAt()
        );
    }

    public Page<ImageData> getImagePage(String username, int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        return imageRepository
                .findByUserUsername(username, pageable)
                .map(this::imageToDto);
    }

    public List<ImageData> uploadImages(MultipartFile[] files) {
        List<Result<String>> results = Arrays
                .stream(files)
                .map(this::trySave)
                .toList();
        return imageRepository.saveAll(
                results
                        .stream()
                        .filter(Result::isOk)
                        .map(Result::unwrap)
                        .map(this::toImageEntity)
                        .toList()
        ).stream()
                .map(this::imageToDto)
                .toList();
    }

    private Image toImageEntity(String name) {
        Image image = new Image();
        //TODO: add correct user; make image private before editing caption etc.?
        image.setImageUrl("/api" + name);
        return image;
    }

    private Result<String> trySave(MultipartFile file) {
        if(Objects.isNull(file.getOriginalFilename()) || file.getOriginalFilename().isEmpty()) {
            return new Result.Err<>(new RuntimeException("Filename cannot be empty!"));
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
            return new Result.Err<>(new RuntimeException("Could not store the file"));
        }
        return new Result.Ok<>(name);
    }
}
