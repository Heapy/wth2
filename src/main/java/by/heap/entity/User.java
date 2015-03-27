package by.heap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
public class User extends AbstractEntity {

    @Column(nullable = false)
    private String displayName;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @OneToMany
    private List<Interest> interests;

    @Column
    private Long longitude;

    @Column
    private Long latitude;


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

    public List<Interest> getInterests() {
        return interests;
    }

    public User setInterests(List<Interest> interests) {
        this.interests = interests;
        return this;
    }

    public Long getLongitude() {
        return longitude;
    }

    public User setLongitude(Long longitude) {
        this.longitude = longitude;
        return this;
    }

    public Long getLatitude() {
        return latitude;
    }

    public User setLatitude(Long latitude) {
        this.latitude = latitude;
        return this;
    }
}
