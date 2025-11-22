package net.partala.user.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO (
        String message,
        String detailedMessage,
        LocalDateTime errorTime
) {
}
