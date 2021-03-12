package com.example.demo.src.my.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostMyCookieUseReq {
    private Integer cookie;
    private Integer contentIndex;
    PostMyCookieUseReq(){};
}