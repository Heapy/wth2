package by.heap.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenPayload {

    @JsonProperty
    public long userId;

    @JsonProperty
    public long expirationTime;

    @Override
    public String toString() {
        return "TokenPayload{" +
                "userId=" + userId +
                ", expirationTime=" + expirationTime +
                '}';
    }
}
