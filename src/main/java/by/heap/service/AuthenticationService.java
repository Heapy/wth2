package by.heap.service;

import by.heap.entity.User;
import by.heap.repository.user.UserRepository;
import by.heap.security.AuthenticationHelper;
import by.heap.security.HeapUserDetails;
import by.heap.service.dto.LoginRequestDto;
import by.heap.service.dto.LoginResponceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationHelper authenticationHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public LoginResponceDto login(@RequestBody final LoginRequestDto request) {
        try {
            LOGGER.debug("Login request recieved.");

            String password = Optional.ofNullable(request.password).orElseThrow(() -> {
                return new BadCredentialsException("Password should be passed.");
            });

            String username = Optional.ofNullable(request.username).orElseThrow(() -> {
                return new BadCredentialsException("Username should be passed.");
            });

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username,
                    password);

            // Try to authenticate with this token
            final Authentication authResult = this.authenticationManager.authenticate(authRequest);

            // Set generated JWT token to response header
            if (authResult.isAuthenticated()) {
                HeapUserDetails userDetails = (HeapUserDetails) authResult.getPrincipal();
                String token = this.authenticationHelper.generateToken(userDetails);
                userDetails.eraseCredentials();
                return new LoginResponceDto(token);
            } else {
                throw new InternalAuthenticationServiceException("Some problem occurred during authentication.");
            }

        } catch (AuthenticationException e) {
            Optional.ofNullable(request.username).ifPresent(username -> {
                LOGGER.warn("Unsuccessful authentication attempt with username '{}'.", username);
            });
            // TODO: Hackaton style
            throw new RuntimeException("An exception during authentication.", e);
        }
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public LoginResponceDto register(@RequestBody final User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return login(new LoginRequestDto(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            Optional.ofNullable(user.getUsername()).ifPresent(username -> {
                LOGGER.warn("Unsuccessful authentication attempt with username '{}'.", username);
            });
            // TODO: Hackaton style
            throw new RuntimeException("An exception during authentication.", e);
        }
    }


}
