package io.github.xpakx.images.image;

import io.github.xpakx.images.image.error.IdCorruptedException;
import io.github.xpakx.images.image.error.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sqids.Sqids;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final Sqids sqids;

    public Image getBySqId(String sqid) {
        List<Long> ids = sqids.decode(sqid);

        if(ids.size() != 1) {
            throw new IdCorruptedException("Id corrupted");
        }
        return imageRepository
                .findById(ids.getFirst())
                .orElseThrow(() -> new ImageNotFoundException("No image with such id"));
    }
}
