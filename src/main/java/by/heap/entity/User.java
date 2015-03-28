package by.heap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
public class User extends AbstractEntity {

    @Column(nullable = false)
    private String displayName;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String secret = UUID.randomUUID().toString();

    @OneToMany
    private List<String> interests = new ArrayList<>();

    @Column
    private Long karma = 0L;

    @Column
    private String longitude;

    @Column
    private String latitude;

    @Column
    private Instant heartbeat;


    public String getDisplayName() {
        return displayName;
    }

    public User setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public User setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public List<String> getInterests() {
        return interests;
    }

    public User setInterests(List<String> interests) {
        this.interests = interests;
        return this;
    }

    public Long getKarma() {
        return karma;
    }

    public User setKarma(Long karma) {
        this.karma = karma;
        return this;
    }

    public String getLongitude() {
        return longitude;
    }

    public User setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getLatitude() {
        return latitude;
    }

    public User setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public Instant getHeartbeat() {
        return heartbeat;
    }

    public User setHeartbeat(Instant heartbeat) {
        this.heartbeat = heartbeat;
        return this;
    }
}
