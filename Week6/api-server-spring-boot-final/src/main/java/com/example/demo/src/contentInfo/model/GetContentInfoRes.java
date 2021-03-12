package com.example.demo.src.contentInfo.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentInfoRes {
    private String title;
    private String content;
    private String musicUrl;
    private float averageStar;
    private String writerComment;
    private int heartSum;
    private int commentSUm;
}