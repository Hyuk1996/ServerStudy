package com.example.demo.src.comment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchCommentLikeStatReq {
    private Integer commentIndex;
    PatchCommentLikeStatReq() {};
}