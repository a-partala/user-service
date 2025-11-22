package net.partala.user.dto;

import jakarta.validation.constraints.*;
import net.partala.user.UserRole;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(

        @Null
        Long id,

        @NotNull
        String login,

        @NotNull
        @Email
        String email,

        @Null
        LocalDateTime registrationDateTime,

        @Null
        Set<UserRole> roles,

        @Null
        boolean emailVerified
) {
}
