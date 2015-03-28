package by.heap.service;


import by.heap.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersService {

    @RequestMapping(value = "top", method = RequestMethod.GET)
    public List<User> top() {
        return new ArrayList<>();
    }
}
