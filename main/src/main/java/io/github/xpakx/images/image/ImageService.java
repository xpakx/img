package io.github.xpakx.images.image;

import io.github.xpakx.images.image.dto.ImageData;
import io.github.xpakx.images.image.error.IdCorruptedException;
import io.github.xpakx.images.image.error.ImageNotFoundException;
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
}
