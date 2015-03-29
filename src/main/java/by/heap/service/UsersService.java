package by.heap.service;


import by.heap.entity.User;
import by.heap.entity.view.UserJsonView;
import by.heap.repository.Pageable;
import by.heap.repository.user.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
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
    @JsonView(UserJsonView.Top.class)
    public List<User> topUsers() {
        return userRepository.findTopByKarma(Pageable.SIX);
    }

    @RequestMapping(value = "location", method = RequestMethod.GET)
    @JsonView(UserJsonView.Location.class)
    public List<User> usersLocation() {
        return userRepository.findAll();
    }
}
