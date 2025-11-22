package net.partala.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record IdRequest(
        @NotNull
        @Min(1)
        Long id
) {
}
