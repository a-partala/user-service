package net.partala.userservice.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String username;

    @Setter
    @Column(unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Column(name = "registration_date_time", nullable = false, updatable = false, insertable = false)
    private LocalDateTime registrationDateTime;

    @Setter
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;
}
