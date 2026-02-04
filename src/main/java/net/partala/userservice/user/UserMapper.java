package net.partala.userservice.user;

import net.partala.userservice.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(UserEntity entity) {

        return new UserResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getRegistrationDateTime(),
                entity.getRoles()
        );
    }
}
