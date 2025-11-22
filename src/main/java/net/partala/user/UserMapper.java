package net.partala.user;

import net.partala.user.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(UserEntity entity) {

        return new UserResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRegistrationDateTime(),
                entity.getRoles(),
                entity.isEmailVerified()
        );
    }
}
