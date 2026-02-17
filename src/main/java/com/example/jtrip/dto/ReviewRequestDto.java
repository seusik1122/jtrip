package com.example.jtrip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private Long placeId;
    private String content;
}
