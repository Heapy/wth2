package by.heap;

import by.heap.entity.User;
import by.heap.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Adds default data to application.
 *
 * @author Ibragimov Ruslan
 */
@Component
public class HeapDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        creteUsers();
    }

    private void creteUsers() {
        Arrays.asList("theliveperson@gmail.com", "test@email.com").forEach(username -> {
            userRepository.save(new User().setDisplayName(username).setUsername(username).setSecret("secret").setPassword(passwordEncoder.encode("qwerty")));
        });
    }

}
