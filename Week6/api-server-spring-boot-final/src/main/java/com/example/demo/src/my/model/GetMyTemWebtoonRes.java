package com.example.demo.src.my.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMyTemWebtoonRes {
    private int numOfTempWebtoon;
    private List<TempWebtoon> tempWebtoons;
}