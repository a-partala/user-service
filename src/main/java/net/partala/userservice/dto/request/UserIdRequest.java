package net.partala.userservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserIdRequest(
        @NotNull
        @Min(1)
        Long userId
) {
}
