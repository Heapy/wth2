package by.heap.security;

import by.heap.entity.User;
import by.heap.repository.user.UserRepository;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Contains info about current user.
 *
 * @author Ibragimov Ruslan
 */
@Component
public class HeapApplicationContext {

    @Autowired
    private UserRepository userRepository;

    public Long getCurrentUserId() {
        return getUserDetails().getUserId();
    }

    public String getCurrentUserName() {
        return getUserDetails().getUsername();
    }

    public User getCurrentUser() {
        if (this.getCurrentUserId() != null) {
            return this.userRepository.findOne(this.getCurrentUserId());
        }
        return null;
    }

    private HeapUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Preconditions.checkNotNull(authentication, "No authentication in context.");
        HeapUserDetails details = (HeapUserDetails) authentication.getPrincipal();
        Preconditions.checkNotNull(details, "No user in context.");
        return details;
    }
}
