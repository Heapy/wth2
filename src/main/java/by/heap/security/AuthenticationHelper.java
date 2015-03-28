package by.heap.security;

import by.heap.repository.user.UserRepository;
import by.heap.service.dto.TokenPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.stereotype.Component;

/**
 * TODO: CommentMe!
 *
 * @author Ibragimov Ruslan
 */
@Component
public class AuthenticationHelper {

    public static final String AUTHENTICATION_HEADER = "Authorization";

    /**
     * TTL for tokens in seconds.
     */
    @Value("${jwt.security.token.expiration.duration:86400}")
    private Long tokenExpirationTime;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    /**
     * Generate JWT from UserDetails.
     *
     * @param userDetails
     * @return JWT converted to string
     */
    public String generateToken(final HeapUserDetails userDetails) {
        try {
            return JwtHelper.encode(this.objectMapper.writeValueAsString(getPayload(userDetails)),
                    new MacSigner(userDetails.getSecret())).getEncoded();
        } catch (JsonProcessingException e) {
            // TODO: Yet another hackatone style
            throw new RuntimeException("Exception during token generation.", e);
        }
    }

    private TokenPayload getPayload(final HeapUserDetails userDetails) {
        TokenPayload payload = new TokenPayload();
        payload.expirationTime = (System.currentTimeMillis() / 1000L) + this.tokenExpirationTime;
        payload.userId = userDetails.getUserId();
        return payload;
    }
}
