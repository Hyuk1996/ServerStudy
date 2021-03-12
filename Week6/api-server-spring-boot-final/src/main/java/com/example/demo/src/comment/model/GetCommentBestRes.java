package com.example.demo.src.comment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentBestRes {
    private int numOfCommment;
    private List<BestComment> bestComments;
}