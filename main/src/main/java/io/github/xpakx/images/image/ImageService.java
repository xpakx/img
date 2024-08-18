package io.github.xpakx.images.image;

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
            // TODO: id corrupted exception
            return null;
        }
        return imageRepository
                .findById(ids.getFirst())
                .orElseThrow(); // TODO: not found exception
    }
}
