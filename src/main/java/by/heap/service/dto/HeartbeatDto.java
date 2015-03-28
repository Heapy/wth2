package by.heap.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HeartbeatDto {

    @JsonProperty
    public String latitude;

    @JsonProperty
    public String longitude;

    @JsonProperty
    public Boolean status;

    @JsonProperty
    public String token;


    public HeartbeatDto(String latitude, String longitude, Boolean status, String token) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.token = token;
    }
}
