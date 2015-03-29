package by.heap.service;

import by.heap.entity.Adventure;
import by.heap.entity.GameStatus;
import by.heap.entity.User;
import by.heap.repository.AdventureRepository;
import by.heap.security.HeapApplicationContext;
import by.heap.service.dto.HeartbeatDto;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
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

    private static final Set<Adventure> PENDING_ADVENTURES = Sets.newConcurrentHashSet();

    private static final Set<Adventure> PLAYING_ADVENTURES = Sets.newConcurrentHashSet();

    @Autowired
    private HeapApplicationContext applicationContext;

    @Autowired
    private AdventureRepository adventureRepository;

    @Scheduled(fixedRate = 3000)
    public void checkForOutdatedUsers() {
        List<Adventure> adventuresToDelete = new ArrayList<>();
        for (Adventure adventure : PENDING_ADVENTURES) {
            // If Adventure has expired user and status of adventure is false (not started)
            if (!adventure.getStatus().get() && isFirstUserExpired(adventure)) {
                LOGGER.info("Adventure with id = '{}' have expired first user.", adventure.getId());
                adventuresToDelete.add(adventure);
            } else if (!adventure.getStatus().get()) {
                LOGGER.info("Adventure with id = '{}' have non expired first user.", adventure.getId());
            }
        }
        PENDING_ADVENTURES.removeAll(adventuresToDelete);
    }

    @Scheduled(fixedRate = 30000)
    public void checkForOutdatedUsersInGame() {
        List<Adventure> adventuresToDelete = new ArrayList<>();
        for (Adventure adventure : PLAYING_ADVENTURES) {
            // If Adventure has expired user and status of adventure is true (started)
            if (adventure.getStatus().get() && isFirstUserExpired(adventure)) {
                LOGGER.info("Adventure with id = '{}' have expired first user.", adventure.getId());
                adventuresToDelete.add(adventure);
            } else if (adventure.getStatus().get() && isSecondUserExpired(adventure)) {
                LOGGER.info("Adventure with id = '{}' have expired second user.", adventure.getId());
                adventuresToDelete.add(adventure);
            }
        }
        PLAYING_ADVENTURES.removeAll(adventuresToDelete);
    }

    private static boolean isFirstUserExpired(Adventure adventure) {
        return isUserExpired(adventure.getFirstUser());
    }

    private static boolean isSecondUserExpired(Adventure adventure) {
        return isUserExpired(adventure.getSecondUser());
    }

    private static boolean isUserExpired(User user) {
        return user.getHeartbeat().getEpochSecond() + 15 < Instant.now().getEpochSecond();
    }

    @RequestMapping(value = "/{id}/heartbeat", method = RequestMethod.PUT)
    public HeartbeatDto heartbeat(@PathVariable Long id, HeartbeatDto heartbeatDto) {
        if (Objects.isNull(heartbeatDto.latitude) || Objects.isNull(heartbeatDto.longitude)) {
            return heartbeatBeforeGame(id);
        } else {
            return heartbeatInGame(id, heartbeatDto);
        }
    }

    private HeartbeatDto heartbeatBeforeGame(Long id) {
        for (Adventure adventure : PENDING_ADVENTURES) {
            if (adventure.getId().equals(id)) {
                adventure.getFirstUser().setHeartbeat(Instant.now());
                break;
            }
        }
        return new HeartbeatDto(id, null, null, GameStatus.SEARCHING, null);
    }


    private HeartbeatDto heartbeatInGame(Long id, HeartbeatDto heartbeatDto) {
        for (Adventure adventure : PLAYING_ADVENTURES) {
            if (adventure.getId().equals(id)) {
                Long currentUserId = applicationContext.getCurrentUserId();
                switch (adventure.getGameStatus()) {
                    case PLAYING:
                        return playing(heartbeatDto, adventure, currentUserId);
                    case AFTER_GAME:
                        return new HeartbeatDto(id, null, null, GameStatus.AFTER_GAME, adventure.getToken());
                }
            }
        }
        return new HeartbeatDto(id, null, null, GameStatus.ERROR, null);
    }

    private HeartbeatDto playing(HeartbeatDto heartbeatDto, Adventure adventure, Long currentUserId) {
        if (adventure.getFirstUser().getId().equals(currentUserId)) {
            adventure.getFirstUser().setLongitude(heartbeatDto.longitude).setLatitude(heartbeatDto.latitude);
            return new HeartbeatDto(adventure.getId(), adventure.getSecondUser().getLatitude(), adventure.getSecondUser()
                .getLongitude(), GameStatus.PLAYING, adventure.getToken());
        } else if (adventure.getSecondUser().getId().equals(currentUserId)) {
            adventure.getSecondUser().setLongitude(heartbeatDto.longitude).setLatitude(heartbeatDto.latitude);
            return new HeartbeatDto(adventure.getId(), adventure.getFirstUser().getLatitude(), adventure.getFirstUser()
                .getLongitude(), GameStatus.PLAYING, adventure.getToken());
        } else {
            LOGGER.error("Приплыли");
            return new HeartbeatDto(adventure.getId(), null, null, GameStatus.ERROR, null);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public HeartbeatDto findMeAdventureOnMyAss(HeartbeatDto heartbeatDto) {
        final User currentUser = applicationContext.getCurrentUser();
        Adventure foundAdventure = null;
        for (Adventure adventure : PENDING_ADVENTURES) {
            User userToCompare = adventure.getFirstUser();
            if (hasSameInterests(currentUser, userToCompare)) {
                if (adventure.getStatus().compareAndSet(false, true)) {
                    adventure.setSecondUser(currentUser);
                    PENDING_ADVENTURES.remove(adventure);
                    PLAYING_ADVENTURES.add(adventure);
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
        adventureRepository.save(adventure);
        PENDING_ADVENTURES.add(adventure);
        return adventure;
    }
}
