package com.example.demo.src.webtoon.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetWebtoonContentInfoRes {
    private String title;
    private String content;
    private String musicUrl;
    private float averageStar;
    private String isGraded;
    private String writerComment;
    private String writer;
    private String isInterested;
    private int heartSum;
    private String ishearted;
    private int commentSum; 
}
