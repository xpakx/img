package io.github.xpakx.images.account;

import io.github.xpakx.images.account.dto.AuthenticationResponse;
import io.github.xpakx.images.account.dto.RegistrationRequest;
import io.github.xpakx.images.account.error.AuthenticationException;
import io.github.xpakx.images.account.error.ValidationException;
import io.github.xpakx.images.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwt;

    public AuthenticationResponse register(RegistrationRequest request) {
        testRequest(request);
        User user = createNewUser(request);
        authenticate(request.username(), request.password());
        final String token = jwt.generateToken(userService.userAccountToUserDetails(user));
        final String refreshToken = jwt.generateRefreshToken(request.username());
        return new AuthenticationResponse(
                token,
                refreshToken,
                user.getUsername(),
                false
            );
    }

    private User createNewUser(RegistrationRequest request) {
        Set<UserRole> roles = new HashSet<>();
        User userToAdd = new User();
        userToAdd.setPassword(passwordEncoder.encode(request.password()));
        userToAdd.setUsername(request.username());
        userToAdd.setRoles(roles);
        return userRepository.save(userToAdd);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User " +username+" disabled!");
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid password!");
        }
    }

    private void testRequest(RegistrationRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new ValidationException("Username exists!");
        }
    }
}
