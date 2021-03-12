package com.example.demo.src.comment;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.comment.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/comments")
public class CommentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CommentProvider commentProvider;
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final JwtService jwtService;


    public CommentController(CommentProvider commentProvider, CommentService commentService, JwtService jwtService){
        this.commentProvider = commentProvider;
        this.commentService = commentService;
        this.jwtService = jwtService;
    }

    /** 댓글 조회 API
     * (GET) /comments/all?userIndex=?&contentIndex=?
     * @return BaseResponse<GetCommentRes>
     */
    @ResponseBody
    @GetMapping("/all") // (GET) https://www.hyukserver.site/app/comments/all?userIndex=?&contentIndex=?
    public BaseResponse<GetCommentRes> getComments(@RequestParam(value="userIndex", required=false, defaultValue="0") int userIndex, @RequestParam(value="contentIndex", required=false, defaultValue="0") int contentIndex){
        if(userIndex == 0){
            return new BaseResponse<>(EMPTY_USERINDEX);
        }
        if(contentIndex == 0) {
            return new BaseResponse<>(EMPTY_CONTENTINDEX);
        }
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

        GetCommentRes getCommentRes = commentProvider.getComments(userIndex, contentIndex);
        return new BaseResponse<>(getCommentRes);
    }

    /** 베스트 댓글 조회 API
     * [GET] /comments/best?userIndex=?&contentIndex=?
     * @return BaseResponse<GetCommentBestRes>
     */
    @ResponseBody
    @GetMapping("/best") // (GET) https://www.hyukserver.site/app/comments/best?userIndex=?&contentIndex=?
    public BaseResponse<GetCommentBestRes> getCommentsBest(@RequestParam(value="userIndex", required=false, defaultValue="0") int userIndex, @RequestParam(value="contentIndex", required=false, defaultValue="0") int contentIndex){
        if(userIndex == 0){
            return new BaseResponse<>(EMPTY_USERINDEX);
        }
        if(contentIndex == 0) {
            return new BaseResponse<>(EMPTY_CONTENTINDEX);
        }
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

        GetCommentBestRes getCommentBestRes = commentProvider.getCommentsBest(userIndex, contentIndex);
        return new BaseResponse<>(getCommentBestRes);
    }

    /** 댓글 작성 API
     * [POST] /comments/:userIndex
     * @return BaseResponse<PostCommentRes>
     */
    @ResponseBody
    @PostMapping("/{userIndex}") // (POST) https://www.hyukserver.site/app/comments/:userIndex
    public BaseResponse<PostCommentRes> postComment(@PathVariable("userIndex") int userIndex, @RequestBody PostCommentReq postCommentReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(postCommentReq.getContentIndex() == null){
            return new BaseResponse<>(EMPTY_CONTENTINDEX);
        }
        if(postCommentReq.getCommentContent() == null){
            return new BaseResponse<>(EMPTY_COMMENT_CONTENT);
        }

        try{
            PostCommentRes postCommentRes = commentService.postComment(userIndex, postCommentReq);
            return new BaseResponse<>(postCommentRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 댓글 삭제 API
     * [PATCH] /comments/:userIndex/status
     * @return BaseResponse<PatchCommentStatRes>
     */
    @ResponseBody
    @PatchMapping("/{userIndex}/status") // (PATCH) https://www.hyukserver.site/app/comments/:userIndex/status
    public BaseResponse<PatchCommentStatRes> patchCommentStat(@PathVariable("userIndex") int userIndex, @RequestBody PatchCommentStatReq patchCommentStatReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(patchCommentStatReq.getContentIndex() == null){
            return new BaseResponse<>(EMPTY_CONTENTINDEX);
        }

        try{
            PatchCommentStatRes patchCommentStatRes = commentService.patchCommentStat(userIndex, patchCommentStatReq.getContentIndex());
            return new BaseResponse<>(patchCommentStatRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 좋아요 추가 API
     * [POST] /comments/:userIndex/like
     * @return BaseResponse<PostCommentLikeRes>
     */
    @ResponseBody
    @PostMapping("/{userIndex}/like") // (POST) https://www.hyukserver.site/app/comments/:userIndex/like
    public BaseResponse<PostCommentLikeRes> postCommentLike(@PathVariable("userIndex") int userIndex, @RequestBody PostCommentLikeReq postCommentLikeReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(postCommentLikeReq.getCommentIndex() == 0){
            return new BaseResponse<>(EMPTY_COMMENTINDEX);
        }

        try{
            PostCommentLikeRes postCommentLikeRes = commentService.postCommentLike(userIndex, postCommentLikeReq.getCommentIndex());
            return new BaseResponse<>(postCommentLikeRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 좋아요 취소 API
     * [PATCH] /comments/:userIndex/like/status
     * @return BaseResponse<PatchCommentLikeStatRes>
     */
    @ResponseBody
    @PatchMapping("/{userIndex}/like/status") // (PATCH) https://www.hyukserver.site/app/comments/:userIndex/like/status
    public BaseResponse<PatchCommentLikeStatRes> patchCommentLikeStat(@PathVariable("userIndex") int userIndex, @RequestBody PatchCommentLikeStatReq patchCommentLikeStatReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(patchCommentLikeStatReq.getCommentIndex() == null){
            return new BaseResponse<>(EMPTY_COMMENTINDEX);
        }

        try{
            PatchCommentLikeStatRes patchCommentLikeStatRes = commentService.patchCommentLikeStat(userIndex, patchCommentLikeStatReq.getCommentIndex());
            return new BaseResponse<>(patchCommentLikeStatRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 싫어요 추가 API
     * [POST] /comments/:userIndex/unlike
     * @return BaseResponse<PostCommentUnlikeRes>
     */
    @ResponseBody
    @PostMapping("/{userIndex}/unlike") // (POST) https://www.hyukserver.site/app/comments/:userIndex/unlike
    public BaseResponse<PostCommentUnlikeRes> postCommentUnlike(@PathVariable("userIndex") int userIndex, @RequestBody PostCommentUnlikeReq postCommentUnlikeReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(postCommentUnlikeReq.getCommentIndex() == null){
            return new BaseResponse<>(EMPTY_COMMENTINDEX);
        }

        try{
            PostCommentUnlikeRes postCommentUnlikeRes = commentService.postCommentUnlike(userIndex, postCommentUnlikeReq.getCommentIndex());
            return new BaseResponse<>(postCommentUnlikeRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 싫어요 삭제 API
     * [PATCH] /comments/:userIndex/unlike/status
     * @return BaseResponse<PatchCommentUnlikeStatRes>
     */
    @ResponseBody
    @PatchMapping("/{userIndex}/unlike/status") // (Patch) https://www.hyukserver.site/app/comments/:userIndex/unlike/status
    public BaseResponse<PatchCommentUnlikeStatRes> patchCommentUnlikeStat(@PathVariable("userIndex") int userIndex, @RequestBody PatchCommentUnlikeStatReq patchCommentUnlikeStatReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(patchCommentUnlikeStatReq.getCommentIndex() == null){
            return new BaseResponse<>(EMPTY_COMMENTINDEX);
        }

        try{
            PatchCommentUnlikeStatRes patchCommentUnlikeStatRes = commentService.patchCommentUnlikeStat(userIndex, patchCommentUnlikeStatReq.getCommentIndex());
            return new BaseResponse<>(patchCommentUnlikeStatRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
