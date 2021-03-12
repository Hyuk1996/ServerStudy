package com.example.demo.src.contentList.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetWebtoonInfoRes {
    private String imageUrl;
    private int interest;
    private String webtoonName;
    private String writer;
    private String serialDate;
    private String introduction;
}