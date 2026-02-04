package net.partala.userservice.dto.request;

import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @NotNull
        String username,
        @NotNull
        String password
) {
}
