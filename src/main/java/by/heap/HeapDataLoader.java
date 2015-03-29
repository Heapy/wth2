package by.heap;

import by.heap.entity.Interest;
import by.heap.entity.User;
import by.heap.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

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
        userRepository.save(new User().setKarma(42L).setDisplayName("Ruslan Ibragimov").setUsername("theliveperson@gmail.com").setSecret("secret").setPassword(passwordEncoder.encode("qwerty")).setAvatar("http://heap.by/assers/avatars/1.jpg"));
        userRepository.save(new User().setKarma(99L).setDisplayName("Hleb Albau").setUsername("hleb.albau@gmail.com").setSecret("secret").setPassword(passwordEncoder.encode("qwerty")).setAvatar("http://heap.by/assers/avatars/2.jpg"));
        userRepository.save(new User().setKarma(44L).setDisplayName("Max Gergalov").setUsername("maxgergalov@gmail.com").setSecret("secret").setPassword(passwordEncoder.encode("qwerty")).setAvatar("http://heap.by/assers/avatars/3.jpg"));
        userRepository.save(new User().setKarma(55L).setDisplayName("Vlad Haevskii").setUsername("kelstar95@gmail.com").setSecret("secret").setPassword(passwordEncoder.encode("qwerty")).setAvatar("http://heap.by/assers/avatars/4.jpg"));
        userRepository.save(new User().setKarma(33L).setDisplayName("Sasha Demeshko").setUsername("demeshko.alexander@gmail.com").setSecret("secret").setPassword(passwordEncoder.encode("qwerty")).setAvatar("http://heap.by/assers/avatars/5.jpg"));
        userRepository.save(new User().setKarma(100L).setDisplayName("Igor Rain").setUsername("ce.fire@tut.by").setSecret("secret").setPassword(passwordEncoder.encode("qwerty")).setAvatar("http://heap.by/assers/avatars/6.jpg"));
        userRepository.save(new User().setKarma(1L).setDisplayName("Oleg Sauko").setUsername("oleg.sauko@gmail.com").setSecret("secret").setPassword(passwordEncoder.encode("qwerty")).setAvatar("http://heap.by/assers/avatars/7.jpg"));
        userRepository.save(new User().setKarma(1L).setDisplayName("Tester 1").setInterests(new HashSet<>(Arrays.asList(new Interest("Java"), new Interest("Boobs")))).setUsername("test1").setSecret("secret").setPassword(passwordEncoder.encode("q")).setAvatar("http://heap.by/assers/avatars/7.jpg"));
        userRepository.save(new User().setKarma(1L).setDisplayName("Tester 2").setInterests(new HashSet<>(Arrays.asList(new Interest("Java"), new Interest("Cars")))).setUsername("test2").setSecret("secret").setPassword(passwordEncoder.encode("q")).setAvatar("http://heap.by/assers/avatars/7.jpg"));
    }

}
