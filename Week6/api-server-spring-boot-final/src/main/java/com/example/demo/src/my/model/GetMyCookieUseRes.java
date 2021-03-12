package com.example.demo.src.my.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMyCookieUseRes {
    private String currentCookie;
    private List<Use> uses;
}