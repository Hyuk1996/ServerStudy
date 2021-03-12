package com.example.demo.src.comment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BestComment {
    private String nickname;
    private String userId;
    private String uploadedAt;
    private String content;
    private int likeSum;
    private String isLiked;
    private int unlikeSum;
    private String isUnliked;
    private String best;
}