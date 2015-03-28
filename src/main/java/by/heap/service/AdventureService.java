package by.heap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(value = "/adventure", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdventureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventureService.class);

    private static final Set<UserHolder> USER_HOLDERS = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Scheduled(fixedRate = 5000)
    public void doSomething() {
        // TODO: Implement logic here
        LOGGER.error(Instant.now().toString());
    }
}
