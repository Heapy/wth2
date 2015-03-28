package by.heap.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponceDto {

    @JsonProperty
    public String token;

    public LoginResponceDto(final String token) {
        this.token = token;
    }
}