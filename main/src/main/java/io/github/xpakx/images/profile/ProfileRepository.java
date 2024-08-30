package io.github.xpakx.images.profile;

import io.github.xpakx.images.search.dto.SearchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserUsername(String username);

    @Query("""
    SELECT new io.github.xpakx.images.search.dto.SearchResult(
      u.username as username,
      p.description as description
    )
    FROM User u
    LEFT JOIN Profile p ON u.id = p.user.id
    WHERE lower(u.username) LIKE lower(concat('%', ?1, '%'))
    """)
    Page<SearchResult> findByUserUsernameContaining(String name, Pageable pageable);
}