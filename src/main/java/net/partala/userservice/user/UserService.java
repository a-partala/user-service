package net.partala.userservice.user;

import net.partala.userservice.dto.request.RegistrationRequest;
import net.partala.userservice.dto.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@org.springframework.stereotype.Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            UserMapper mapper,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse getUserById(Long id) {
        return mapper.toResponse(getUserEntityById(id));
    }

    //For service-layer use only
    public UserEntity getUserEntityById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Cannot find user with id = " + id
                ));
    }

    public void createUser(
            RegistrationRequest registrationRequest
    ) {
        var userToSave = new UserEntity();
        userToSave.setUsername(registrationRequest.username());
        userToSave.setPassword(passwordEncoder.encode(registrationRequest.password()));
        userToSave.setEmailVerified(false);
        userToSave.setRegistrationDateTime(LocalDateTime.now());

        var role = repository.findAny().isEmpty() ?
                UserRole.ADMIN :
                UserRole.USER;
        userToSave.setRoles(new HashSet<>(Set.of(role)));

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

        userToPromote.setRoles(new HashSet<>(Set.of(UserRole.ADMIN)));

        var savedUser = repository.save(userToPromote);

        return mapper.toResponse(savedUser);
    }

    public void verifyEmail(Long userId, String email) {

        var userEntity = getUserEntityById(userId);
        userEntity.setEmail(email);
        userEntity.setEmailVerified(true);
        repository.save(userEntity);
    }
}
