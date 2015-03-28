package by.heap.security.provider;

import by.heap.entity.User;
import by.heap.repository.user.UserRepository;
import by.heap.security.HeapUserDetails;
import by.heap.service.dto.TokenPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(final Authentication authRequest) throws AuthenticationException {

        // Getting string token from authentication request object
        String token = Optional.ofNullable(StringUtils.trimAllWhitespace((String) authRequest.getCredentials())).orElseThrow(
                () -> new BadCredentialsException("Token not found."));

        // Getting JWT object from string token
        Jwt jwt = JwtHelper.decode(token);

        // Getting payload of token
        String claims = jwt.getClaims();
        TokenPayload tokenPayload;
        try {
            tokenPayload = this.objectMapper.readValue(claims, TokenPayload.class);
        } catch (IOException e) {
            throw new BadCredentialsException("Exception during token parsing.", e);
        }

        // Checking if token already expired and throwing an AuthenticationException in this case
        checkIsExpired(tokenPayload.expirationTime);

        // Getting user id from token
        Long userId = Optional.ofNullable(tokenPayload.userId).orElseThrow(
                () -> new BadCredentialsException("Token doesn't contains user id."));

        // Getting user from database
        User user = this.userRepository.findOne(userId);

        // Getting secret from user
        String secret = Optional.ofNullable(user)
                .orElseThrow(() -> new BadCredentialsException("User for provided token not found."))
                .getSecret();

        // Validate token signature (to be sure that token doesn't change on client side)
        try {
            jwt.verifySignature(new MacSigner(secret));
        } catch (Exception e) {
            throw new BadCredentialsException("Token have invalid signature.");
        }

        // Return authenticated Authentication
        HeapUserDetails userDetails = new HeapUserDetails(user);
        userDetails.eraseCredentials();
        return new JwtAuthenticationToken(userDetails);
    }

    private void checkIsExpired(final Long tokenExpirationTime) {
        if ((System.currentTimeMillis() / 1000L) > tokenExpirationTime) {
            throw new BadCredentialsException("Token expired.");
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
