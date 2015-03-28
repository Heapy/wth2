package by.heap.service;

import by.heap.entity.Adventure;

import java.time.Instant;

/**
 * TODO: CommentMe!
 *
 * @author Ibragimov Ruslan
 */
public class UserHolder {

    // Time of latest heartbeat
    private Instant instant;

    private Adventure adventure;

    public Instant getInstant() {
        return instant;
    }

    public UserHolder setInstant(Instant instant) {
        this.instant = instant;
        return this;
    }

    public Adventure getAdventure() {
        return adventure;
    }

    public UserHolder setAdventure(Adventure adventure) {
        this.adventure = adventure;
        return this;
    }
}
