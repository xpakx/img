package io.github.xpakx.images.feed;

import io.github.xpakx.images.image.dto.ImageData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeedController {
    private final FeedService service;

    @GetMapping("/likes")
    public Page<ImageData> getLiked(@RequestParam int page, Principal principal) {
        return service.getLiked(principal.getName(), page);
    }
}
