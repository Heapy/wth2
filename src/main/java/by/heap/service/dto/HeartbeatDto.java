package by.heap.service.dto;

import by.heap.entity.GameStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HeartbeatDto {

    @JsonProperty
    public Long id;

    @JsonProperty
    public String latitude;

    @JsonProperty
    public String longitude;

    @JsonProperty
    public GameStatus status;

    @JsonProperty
    public String token;

    public HeartbeatDto() {
    }

    public HeartbeatDto(Long id, String latitude, String longitude, GameStatus status, String token) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.token = token;
    }
}
