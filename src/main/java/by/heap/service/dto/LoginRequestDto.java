package by.heap.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestDto {

    @JsonProperty
    public String username;

    @JsonProperty
    public String password;

    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequestDto() {
    }

    @Override
    public String toString() {
        return "LoginRequestDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}