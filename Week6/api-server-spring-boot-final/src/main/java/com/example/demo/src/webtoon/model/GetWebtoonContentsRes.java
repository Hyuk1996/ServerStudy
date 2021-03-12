package com.example.demo.src.webtoon.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetWebtoonContentsRes {
    private WebtoonInfo webtoonInfo;
    private List<Content> contents;
}
