package net.partala.userservice.user;

import net.partala.userservice.dto.request.RegistrationRequest;
import net.partala.userservice.dto.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@org.springframework.stereotype.Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(
            UserRepository repository,
            UserMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public UserResponse getUserById(Long id) {
        return mapper.toResponse(getUserEntityById(id));
    }

    private UserEntity getUserEntityById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Cannot find user with id = " + id
                ));
    }

    @Transactional
    public void createUser(
            RegistrationRequest registrationRequest
    ) {

        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.USER);
        if(!repository.existsBy()) {
            roles.add(UserRole.ADMIN);
        }

        var userToSave = UserEntity.builder()
            .username(registrationRequest.username())
            .password(registrationRequest.password())
            .roles(roles)
            .build();

        try {
            var savedUser = repository.save(userToSave);
            log.info("User created successfully, id={}", savedUser.getId());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("This user already exists");
        }
    }

    @Transactional
    public UserResponse promote(Long id) {

        var userToPromote = getUserEntityById(id);

        if(userToPromote.getRoles().contains(UserRole.ADMIN)) {
            throw new IllegalStateException("User is ADMIN already");
        }

        Set<UserRole> roles = new HashSet<UserRole>(userToPromote.getRoles());
        roles.add(UserRole.ADMIN);
        userToPromote.setRoles(roles);

        var savedUser = repository.save(userToPromote);

        return mapper.toResponse(savedUser);
    }

    @Transactional
    public void verifyEmail(Long userId, String email) {

        var userEntity = getUserEntityById(userId);
        userEntity.setEmail(email);
        repository.save(userEntity);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
