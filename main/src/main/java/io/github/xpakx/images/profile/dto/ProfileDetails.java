package io.github.xpakx.images.profile.dto;

public record ProfileDetails(
        String username,
        String description,
        Long posts,
        Long followers,
        Long following,
        boolean followed,
        boolean owner,
        String avatarUrl
) {
}
