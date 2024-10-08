package io.github.xpakx.images.image;

import io.github.xpakx.images.common.types.ResourceResult;
import io.github.xpakx.images.image.dto.ImageData;
import io.github.xpakx.images.image.dto.ImageDetails;
import io.github.xpakx.images.image.dto.UpdateImageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService service;

    @GetMapping("/image/{id}")
    public ImageData getImageById(@PathVariable String id) {
        return service.getBySqId(id);
    }

    @GetMapping("/image/{id}/details")
    public ImageDetails getImageDetailsById(@PathVariable String id, Principal principal) {
        return service.getImageDetailsBySqId(id, principal.getName());
    }

    @GetMapping("/user/{username}/images")
    public Page<ImageData> getImagesByUsername(@PathVariable String username, @RequestParam int page) {
        return service.getImagePage(username, page);
    }

    @PostMapping("/image")
    @ResponseBody
    public List<ImageData> uploadFiles(@RequestParam("files") MultipartFile[] files, Principal principal) {
        return service.uploadImages(files, principal.getName());
    }

    @GetMapping("/image/file/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        ResourceResult resource = service.getImage(filename);
        return ResponseEntity.ok()
                .contentType(resource.type())
                .body(resource.resource());
    }

    @DeleteMapping("/image/{id}")
    public void deleteImage(@PathVariable String id, Principal principal) {
        service.deleteImage(id, principal.getName());
    }

    @PutMapping("/image/{id}/data")
    public ImageData updateImage(
            @Valid @RequestBody UpdateImageRequest request,
            @PathVariable String id,
            Principal principal
    ) {
        return service.updateImage(request, id, principal.getName());
    }
}
