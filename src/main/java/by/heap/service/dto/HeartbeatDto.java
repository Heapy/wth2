package by.heap.service.dto;

import by.heap.entity.GameStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HeartbeatDto {

    @JsonProperty
    public String latitude;

    @JsonProperty
    public String longitude;

    @JsonProperty
    public GameStatus status;

    @JsonProperty
    public String token;


    public HeartbeatDto(String latitude, String longitude, GameStatus status, String token) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.token = token;
    }

    public HeartbeatDto() {
    }
}
