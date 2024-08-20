package io.github.xpakx.images.image;

import io.github.xpakx.images.image.dto.ImageData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService service;

    @GetMapping("/image/{id}")
    public ImageData getImageById(@PathVariable String id) {
        return service.getBySqId(id);
    }
}
