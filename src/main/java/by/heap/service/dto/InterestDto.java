package by.heap.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Влад on 29.03.2015.
 */
public class InterestDto {
    @JsonProperty
    private String interest;

    public InterestDto() {
    }

    public InterestDto(String interest) {
        this.interest = interest;
    }

    public String getInterest() {
        return interest;
    }

    public InterestDto setInterest(String interest) {
        this.interest = interest;
        return this;
    }
}
