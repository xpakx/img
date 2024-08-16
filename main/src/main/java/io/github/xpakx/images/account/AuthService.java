package io.github.xpakx.images.account;

import io.github.xpakx.images.account.dto.AuthenticationRequest;
import io.github.xpakx.images.account.dto.AuthenticationResponse;
import io.github.xpakx.images.account.dto.RegistrationRequest;
import io.github.xpakx.images.account.dto.RefreshTokenRequest;
import io.github.xpakx.images.account.error.AuthenticationException;
import io.github.xpakx.images.account.error.ValidationException;
import io.github.xpakx.images.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

    public AuthenticationResponse generateAuthenticationToken(AuthenticationRequest authenticationRequest) {
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.username());
        authenticate(authenticationRequest.username(), authenticationRequest.password());
        final String token = jwt.generateToken(userDetails);
        final String refreshToken = jwt.generateRefreshToken(authenticationRequest.username());
        return new AuthenticationResponse(
                token,
                refreshToken,
                authenticationRequest.username(),
                userDetails.getAuthorities().stream()
                        .anyMatch((a) -> a.getAuthority().equals("MODERATOR"))
            );
    }

    public AuthenticationResponse refresh(RefreshTokenRequest request) {
        if(jwt.isInvalid(request.token())) {
            return null;
        }
        Claims claims = jwt.getAllClaimsFromToken(request.token());
        Boolean isRefreshToken = claims.get("refresh", Boolean.class);
        if (Boolean.FALSE.equals(isRefreshToken)) {
            return null;
        }

        var username = claims.getSubject();
        final UserDetails userDetails = userService.loadUserByUsername(username);

        final String token = jwt.generateToken(userDetails);
        final String refreshToken = jwt.generateRefreshToken(username);
        return new AuthenticationResponse(
                token,
                refreshToken,
                username,
                userDetails.getAuthorities().stream()
                        .anyMatch((a) -> a.getAuthority().equals("MODERATOR"))
                );
    }
}
