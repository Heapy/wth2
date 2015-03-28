package by.heap.repository;

import by.heap.entity.Interest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterestRepository extends AbstractRepository<Interest> {

    @Query("select i from User u join u.interests i where u.id = :userId")
    List<Interest> getUserInterests(@Param("userId") Long userId);
}
