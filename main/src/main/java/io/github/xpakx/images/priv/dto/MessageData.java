package io.github.xpakx.images.priv.dto;

public record MessageData(
        Long id,
        String content,
        String sender,
        String senderAvatar,
        String receiver,
        String receiverAvatar
        ) {
}
