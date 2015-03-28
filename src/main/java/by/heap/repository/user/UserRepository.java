package by.heap.repository.user;

import by.heap.entity.User;
import by.heap.repository.AbstractRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserRepository extends AbstractRepository<User> {

    User getUserByUsername(String username);

    @Query("select max(u.karma) from User u")
    List<User> findTopByKarma(Pageable pageable);
}
