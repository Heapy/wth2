package by.heap.service;

import by.heap.entity.Interest;
import by.heap.entity.User;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class AdventureServiceTest {

    @Test
    public void testUserHasSameInterestsTrue() throws Exception {
        User user1 = new User().setInterests(new HashSet<>(Arrays.asList(
            new Interest().setName("Java"),
            new Interest().setName("Movie"),
            new Interest().setName("Boobs")
        )));
        User user2 = new User().setInterests(new HashSet<>(Arrays.asList(
            new Interest().setName("Drinks"),
            new Interest().setName("Music"),
            new Interest().setName("Java")
        )));
        AdventureService adventureService = new AdventureService();
        assertTrue(adventureService.hasSameInterests(user1, user2));

    }

    @Test
    public void testUserHasSameInterestsFalse() throws Exception {
        User user1 = new User().setInterests(new HashSet<>(Arrays.asList(
            new Interest().setName("Java"),
            new Interest().setName("Movie"),
            new Interest().setName("Boobs")
        )));
        User user2 = new User().setInterests(new HashSet<>(Arrays.asList(
            new Interest().setName("Drinks"),
            new Interest().setName("Music"),
            new Interest().setName("Sex")
        )));
        AdventureService adventureService = new AdventureService();
        assertFalse(adventureService.hasSameInterests(user1, user2));

    }

    @Test
    public void testIsUserExpired() throws Exception {
        User user = new User().setHeartbeat(Instant.now());
        Method method = AdventureService.class.getDeclaredMethod("isUserExpired", User.class);
        method.setAccessible(true);

        assertFalse((Boolean) ReflectionUtils.invokeMethod(method, null, user));
    }
}