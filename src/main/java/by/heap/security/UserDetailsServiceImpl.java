package by.heap.security;

import by.heap.entity.User;
import by.heap.repository.user.UserRepository;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * TODO: CommentMe!
 *
 * @author Ibragimov Ruslan
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public HeapUserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(email)) {
            throw new UsernameNotFoundException("No email passed.");
        }

        User user = userRepository.getUserByUsername(email);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException(String.format("User with email=%s was not found", email));
        } else {
            return new HeapUserDetails(user);
        }
    }
}
