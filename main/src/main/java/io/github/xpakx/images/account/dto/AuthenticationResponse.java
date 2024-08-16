package io.github.xpakx.images.account.dto;

public record AuthenticationResponse (
    String token,
    String refresh_token,
    String username,
    boolean moderator_role
) {}
