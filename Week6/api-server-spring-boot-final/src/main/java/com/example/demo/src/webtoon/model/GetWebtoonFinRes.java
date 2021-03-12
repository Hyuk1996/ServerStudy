package com.example.demo.src.webtoon.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetWebtoonFinRes {
    private String imageUrl;
    private String webtoonName;
    private float averageStar;
    private String writer;
}
