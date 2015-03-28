package by.heap.service;

import by.heap.entity.Adventure;
import by.heap.entity.User;
import by.heap.repository.AbstractRepository;
import by.heap.repository.AdventureRepository;
import by.heap.security.HeapApplicationContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

@RestController
@RequestMapping(value = "/adventure", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdventureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventureService.class);

    private static final Set<UserHolder> USER_HOLDERS = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private IntStream randomTokens = new Random().ints().filter(i -> i > 100000 && i < 999999);

    @Autowired
    private HeapApplicationContext applicationContext;

    @Autowired
    private AdventureRepository adventureRepository;

    @Scheduled(fixedRate = 5000)
    public void doSomething() {
        // TODO: Implement logic here
        LOGGER.error(Instant.now().toString());
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Adventure heartbeat() {
        final User currentUser = applicationContext.getCurrentUser();
        Adventure currentAdventure = null;
        for (UserHolder userHolder : USER_HOLDERS) {
            if (userHolder.getAdventure().getFirstUser().equals(currentUser)) {
                userHolder.setInstant(Instant.now());
                currentAdventure = userHolder.getAdventure();
                break;
            }
        }
        return currentAdventure;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Adventure findMeAdventureOnMyAss() {
        final User currentUser = applicationContext.getCurrentUser();
        Adventure foundAdventure = null;
        for (UserHolder userHolder : USER_HOLDERS) {
            User userToCompare = userHolder.getAdventure().getFirstUser();
            if (hasSameInterests(currentUser, userToCompare)) {
                if (userHolder.getAdventure().getStatus().compareAndSet(false, true)) {
                    foundAdventure = userHolder.getAdventure();
                    foundAdventure.setSecondUser(currentUser);
                    USER_HOLDERS.remove(userHolder);
                    //TODO в список играющих
                    break;
                }
            }
        }
        if (foundAdventure == null) {
            foundAdventure = createNewAdventureAndWait(currentUser);
        }
        return foundAdventure;
    }

    public boolean hasSameInterests(User user1, User user2) {
        return !Sets.intersection(user1.getInterests(), user2.getInterests()).isEmpty();
    }

    private Adventure createNewAdventureAndWait(User user) {
        Adventure newAdventure = new Adventure()
            .setFirstUser(user)
            .setStatus(new AtomicBoolean(false))
            .setToken(String.valueOf(randomTokens.findAny().getAsInt()));
        adventureRepository.save(newAdventure);
        USER_HOLDERS.add(
            new UserHolder()
                .setInstant(Instant.now())
                .setAdventure(newAdventure)
        );
        return newAdventure;
    }

}
