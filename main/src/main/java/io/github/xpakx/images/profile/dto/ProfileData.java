package io.github.xpakx.images.profile.dto;

public record ProfileData(
        Long id,
        String username,
        String description,
        boolean avatar
) { }
