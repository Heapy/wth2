package by.heap.service;


import by.heap.entity.User;
import by.heap.repository.Pageable;
import by.heap.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "top", method = RequestMethod.GET)
    public List<User> topUsers() {
        return userRepository.findTopByKarma(Pageable.SEVEN);
    }

    @RequestMapping(value = "location", method = RequestMethod.GET)
    public List<User> usersLocation() {
        return userRepository.findAll();
    }
}
