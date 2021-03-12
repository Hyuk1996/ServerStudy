package com.example.demo.src.webtoon.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class WebtoonInfo {
    private String imageUrl;
    private String isInterested;
    private String interestedSum;
    private String webtoonName;
    private String writer;
    private String serialDay;
    private String introduction;
    private String numberOfPreview;
}
