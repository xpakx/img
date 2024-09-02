package io.github.xpakx.images.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "User")
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String avatarUrl;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    nullable = false,
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_role_id",
                    nullable = false,
                    referencedColumnName = "id"
            )
    )
    private Set<UserRole> roles;

    @PrePersist
    protected void onCreate() {
        this.avatarUrl = "avatars/default.jpg";
    }
}
