package by.heap.service;

import by.heap.entity.User;
import by.heap.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public User updateUser(@PathVariable Long id) {
        return null; //TODO
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long id) {
        return userRepository.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public User updateCurrentUser() {
        return null; //TODO
    }

    @RequestMapping(method = RequestMethod.GET)
    public User getCurrentUser() {
        return userRepository.gerCurrentUser();
    }

}
