package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserJoinReq {
    private String userId;
    private String nickname;
    private String userPw;
    private String gender;
}
