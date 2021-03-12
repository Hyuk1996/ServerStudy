package com.example.demo.src.contentList.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentListRes {
    private String representativeImageUrl;
    private String title;
    private float averageStar;
    private String uploadedAt;
    private int cookieValue;
    private String hasSeen;
}