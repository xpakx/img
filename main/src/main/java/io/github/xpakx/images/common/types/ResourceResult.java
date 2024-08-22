package io.github.xpakx.images.common.types;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public record ResourceResult(Resource resource, MediaType type) {
}
