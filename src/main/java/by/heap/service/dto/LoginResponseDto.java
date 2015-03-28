package by.heap.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponseDto {

    @JsonProperty
    public String token;

    public LoginResponseDto(final String token) {
        this.token = token;
    }

    public LoginResponseDto() {
    }
}