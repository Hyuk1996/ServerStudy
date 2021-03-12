package com.example.demo.src.webtoon.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostWebtoonStarReq {
    private Integer contentIndex;
    private Float grade;
    PostWebtoonStarReq(){};
}
