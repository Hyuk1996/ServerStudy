package com.example.demo.src.webtoon.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Content {
    private String contentImageUrl;
    private String title;
    private float averageStar;
    private String uploadedAt;
    private String isReaded;
}
