package net.partala.userservice.dto.response;

import jakarta.validation.constraints.*;
import net.partala.userservice.user.UserRole;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(

        @Null
        Long id,

        @NotNull
        String username,

        @Null
        LocalDateTime registrationDateTime,

        @Null
        Set<UserRole> roles
) {
}
