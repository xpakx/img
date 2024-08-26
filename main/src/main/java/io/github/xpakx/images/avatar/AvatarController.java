package io.github.xpakx.images.avatar;

import io.github.xpakx.images.common.types.ResourceResult;
import io.github.xpakx.images.image.dto.ImageData;
import io.github.xpakx.images.image.dto.ImageDetails;
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
public class AvatarController {
    private final AvatarService service;

    @PostMapping("/avatar")
    @ResponseBody
    public ResponseEntity<?> uploadAvatar(@RequestParam("files") MultipartFile file, Principal principal) {
        service.uploadAvatar(file, principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{username}/avatar")
    public ResponseEntity<Resource> getImage(@PathVariable String username) {
        ResourceResult resource = service.getAvatar(username);
        return ResponseEntity.ok()
                .contentType(resource.type())
                .body(resource.resource());
    }
}
