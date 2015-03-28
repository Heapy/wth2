package by.heap.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestDto {

    @JsonProperty
    public String username;

    @JsonProperty
    public String password;
}