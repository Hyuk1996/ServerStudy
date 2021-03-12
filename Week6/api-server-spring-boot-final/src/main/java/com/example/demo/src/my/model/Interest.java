package com.example.demo.src.my.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Interest {
    private String imageUrl;
    private String webtoonName;
    private String uploadedAt;
    private String isAlarmed;
}