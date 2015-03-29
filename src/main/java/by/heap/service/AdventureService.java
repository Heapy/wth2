package by.heap.service;

import by.heap.entity.Adventure;
import by.heap.entity.GameStatus;
import by.heap.entity.Interest;
import by.heap.entity.User;
import by.heap.repository.AdventureRepository;
import by.heap.repository.user.UserRepository;
import by.heap.security.HeapApplicationContext;
import by.heap.service.dto.HeartbeatDto;
import by.heap.service.dto.InterestDto;
import by.heap.service.dto.StatusDto;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping(value = "/adventure", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdventureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventureService.class);

    private static final Set<Adventure> ADVENTURES = Sets.newConcurrentHashSet();

    @Autowired
    private HeapApplicationContext applicationContext;

    @Autowired
    private AdventureRepository adventureRepository;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 3000)
    public void checkForOutdatedUsers() {
        for (Adventure adventure : ADVENTURES) {
            // If Adventure has expired user and status of adventure is false (not started)
            if (GameStatus.SEARCHING.equals(adventure.getGameStatus()) && isFirstUserExpired(adventure, 60)) {
                LOGGER.info("Adventure with id = '{}' have expired first user with username = '{}'.", adventure.getId(), adventure.getFirstUser().getUsername());
                adventure.setGameStatus(GameStatus.EXPIRED);
            } else if (GameStatus.SEARCHING.equals(adventure.getGameStatus())) {
                LOGGER.info("Adventure with id = '{}' have non expired first user with username = '{}'.", adventure.getId(), adventure.getFirstUser().getUsername());
            }
        }
    }

    @Scheduled(fixedRate = 30000)
    public void checkForOutdatedUsersInGame() {
        for (Adventure adventure : ADVENTURES) {
            if (Objects.nonNull(adventure.getFirstUser())) {
                userRepository.save(adventure.getFirstUser());
            }
            if (Objects.nonNull(adventure.getSecondUser())) {
                userRepository.save(adventure.getSecondUser());
            }
            // If Adventure has expired user and status of adventure is true (started)
            if (GameStatus.PLAYING.equals(adventure.getGameStatus()) && isFirstUserExpired(adventure, 60)) {
                LOGGER.info("Adventure with id = '{}' have expired first user with username = '{}'.", adventure.getId(), adventure.getFirstUser().getUsername());
                adventure.setGameStatus(GameStatus.EXPIRED);
            } else if (GameStatus.PLAYING.equals(adventure.getGameStatus()) && isSecondUserExpired(adventure, 60)) {
                LOGGER.info("Adventure with id = '{}' have expired second user with username = '{}'.", adventure.getId(), adventure.getSecondUser().getUsername());
                adventure.setGameStatus(GameStatus.EXPIRED);
            }
        }
    }

    private static boolean isFirstUserExpired(Adventure adventure, Integer seconds) {
        return isUserExpired(adventure.getFirstUser(), seconds);
    }

    private static boolean isSecondUserExpired(Adventure adventure, Integer seconds) {
        return isUserExpired(adventure.getSecondUser(), seconds);
    }

    private static boolean isUserExpired(User user, Integer seconds) {
        return user.getHeartbeat().getEpochSecond() + seconds < Instant.now().getEpochSecond();
    }

    @RequestMapping(value = "/{id}/token", method = RequestMethod.POST)
    public StatusDto token(@PathVariable Long id, @RequestBody HeartbeatDto heartbeatDto) {
        for (Adventure adventure : ADVENTURES) {
            if (id.equals(adventure.getId())) {
                if (adventure.getToken().equals(heartbeatDto.token)) {
                    adventure.setGameStatus(GameStatus.AFTER_GAME);
                    User user = applicationContext.getCurrentUser();
                    user.setKarma(user.getKarma() + 1);
                    userRepository.save(user);
                    return new StatusDto(true);
                }
            }
        }
        return new StatusDto(false);
    }

    @RequestMapping(value = "/{id}/interest", method = RequestMethod.POST)
    public StatusDto checkInterest(@PathVariable Long id, @RequestBody InterestDto interestDto) {
        Long currentUserId = applicationContext.getCurrentUserId();
        Interest suggestedInterest = new Interest(interestDto.getInterest());
        for (Adventure adventure : ADVENTURES) {
            if (id.equals(adventure.getId())) {
                if (adventure.getFirstUser().getId().equals(currentUserId)) {
                    return validateInterest(adventure.getSecondUser(), suggestedInterest);
                } else {
                    return validateInterest(adventure.getFirstUser(), suggestedInterest);
                }
            }
        }
        return new StatusDto(false);
    }

    private StatusDto validateInterest(User anotherPlayer, Interest suggestedInterest) {
        if (anotherPlayer.getInterests().contains(suggestedInterest)) {
            User user = applicationContext.getCurrentUser();
            user.setKarma(user.getKarma() + 5);
            userRepository.save(user);
            return new StatusDto(true);
        } else {
            return new StatusDto(false);
        }
    }


    @RequestMapping(value = "/{id}/heartbeat", method = RequestMethod.PUT)
    public HeartbeatDto heartbeat(@PathVariable Long id, @RequestBody HeartbeatDto heartbeatDto) {

        LOGGER.info("Heartbeat: user = {}, id = {}, dto = {}.", applicationContext.getCurrentUser().getUsername(), id, heartbeatDto);

        for (Adventure adventure : ADVENTURES) {
            if (adventure.getId().equals(id)) {
                switch (adventure.getGameStatus()) {
                    case SEARCHING:
                        return searching(heartbeatDto, adventure);
                    case PLAYING:
                        return playing(heartbeatDto, adventure);
                    case AFTER_GAME:
                        return new HeartbeatDto(id, null, null, GameStatus.AFTER_GAME, adventure.getToken());
                    case EXPIRED:
                        return new HeartbeatDto(id, null, null, GameStatus.EXPIRED, adventure.getToken());
                }
            }
        }
        return new HeartbeatDto(id, null, null, GameStatus.ERROR, "Error in heartbeat(). No adventure find in pending adventures.");
    }

    private HeartbeatDto searching(HeartbeatDto heartbeatDto, Adventure adventure) {
            adventure.getFirstUser().setHeartbeat(Instant.now());
            adventure.getFirstUser().setLongitude(heartbeatDto.longitude);
            adventure.getFirstUser().setLatitude(heartbeatDto.latitude);
            return new HeartbeatDto(adventure.getId(), null, null, GameStatus.SEARCHING, adventure.getToken());
    }

    private HeartbeatDto playing(HeartbeatDto heartbeatDto, Adventure adventure) {
        Long currentUserId = applicationContext.getCurrentUserId();

        if (adventure.getFirstUser().getId().equals(currentUserId)) {
            adventure.getFirstUser().setLongitude(heartbeatDto.longitude).setLatitude(heartbeatDto.latitude).setHeartbeat(Instant.now());
            return new HeartbeatDto(adventure.getId(), adventure.getSecondUser().getLatitude(), adventure.getSecondUser()
                .getLongitude(), GameStatus.PLAYING, adventure.getToken());
        } else if (adventure.getSecondUser().getId().equals(currentUserId)) {
            adventure.getSecondUser().setLongitude(heartbeatDto.longitude).setLatitude(heartbeatDto.latitude).setHeartbeat(Instant.now());
            return new HeartbeatDto(adventure.getId(), adventure.getFirstUser().getLatitude(), adventure.getFirstUser()
                .getLongitude(), GameStatus.PLAYING, adventure.getToken());
        } else {
            LOGGER.error("Приплыли");
            return new HeartbeatDto(adventure.getId(), null, null, GameStatus.ERROR, "Error in playing()");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public HeartbeatDto findMeAdventureOnMyAss(@RequestBody HeartbeatDto heartbeatDto) {
        final User currentUser = applicationContext.getCurrentUser();
        Adventure foundAdventure = null;
        for (Adventure adventure : ADVENTURES) {
            User userToCompare = adventure.getFirstUser();
            LOGGER.info("Test if have same interest firstUser = '{}', secondUser = '{}'", userToCompare, currentUser);
            if (hasSameInterests(currentUser, userToCompare)) {
                if (adventure.getStatus().compareAndSet(false, true)) {
                    adventure.setSecondUser(currentUser);
                    adventure.setGameStatus(GameStatus.PLAYING);
                    currentUser.setHeartbeat(Instant.now()).setLatitude(heartbeatDto.latitude).setLongitude(heartbeatDto.longitude);
                    LOGGER.warn("Adventure '{}' started.", adventure);
                    foundAdventure = adventure;
                    break;
                }
            }
        }
        if (foundAdventure == null) {
            foundAdventure = createNewAdventureAndWait(currentUser);
        }
        return new HeartbeatDto(
            foundAdventure.getId(),
            foundAdventure.getFirstUser().getLatitude(),
            foundAdventure.getFirstUser().getLongitude(),
            foundAdventure.getGameStatus(),
            foundAdventure.getToken()
        );
    }

    public boolean hasSameInterests(User user1, User user2) {
        return !Sets.intersection(user1.getInterests(), user2.getInterests()).isEmpty();
    }

    private Adventure createNewAdventureAndWait(User user) {
        Adventure adventure = new Adventure()
                .setFirstUser(user)
                .setStatus(new AtomicBoolean(false))
                .setGameStatus(GameStatus.SEARCHING)
                .setToken(String.valueOf(new Random().ints(100000, 1000000).findAny().getAsInt()));
        adventure.getFirstUser().setHeartbeat(Instant.now());
        ADVENTURES.add(adventure);
        adventureRepository.save(adventure);
        LOGGER.warn("Adventure '{}' is pending.", adventure);
        return adventure;
    }
}
