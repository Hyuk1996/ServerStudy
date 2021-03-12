package com.example.demo.src.webtoon.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetWebtoonRes {
    private String imageUrl;
    private String webtoonName;
    private float averageStar;
    private String writer;
    private String isUploaded;
}
