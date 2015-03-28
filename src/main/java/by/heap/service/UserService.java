package by.heap.service;

import by.heap.entity.User;
import by.heap.repository.user.UserRepository;
import by.heap.security.HeapApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HeapApplicationContext applicationContext;

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public User updateUser(@PathVariable Long id, User update) {
        return update(userRepository.findOne(id), update);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long id) {
        return userRepository.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public User updateCurrentUser(User update) {
        User user = applicationContext.getCurrentUser();
        return update(user, update);
    }

    private User update(User user, User update) {
        if (Objects.nonNull(update.getDisplayName())) {
            user.setDisplayName(update.getDisplayName());
        }
        if (Objects.nonNull(update.getKarma())) {
            user.setKarma(update.getKarma());
        }
        if (Objects.nonNull(update.getLatitude())) {
            user.setLatitude(update.getLatitude());
        }
        if (Objects.nonNull(update.getLongitude())) {
            user.setLongitude(update.getLongitude());
        }
        if (Objects.nonNull(update.getUsername())) {
            user.setUsername(update.getUsername());
        }

        return userRepository.save(user);
    }

    @RequestMapping(method = RequestMethod.GET)
    public User getCurrentUser() {
        return applicationContext.getCurrentUser();
    }
}
