package by.heap.service;

import org.eclipse.jetty.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping(value = "/adventure", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdventureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventureService.class);

    private static final ConcurrentHashSet<UserHolder> USER_HOLDERS = new ConcurrentHashSet<>();

    @Scheduled(fixedRate = 5000)
    public void doSomething() {
        // TODO: Implement logic here
        LOGGER.error(Instant.now().toString());
    }
}
