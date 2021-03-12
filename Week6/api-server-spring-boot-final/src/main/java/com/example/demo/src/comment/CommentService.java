package com.example.demo.src.comment;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.comment.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class CommentService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentDao commentDao;
    private final CommentProvider commentProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public CommentService(CommentDao commentDao, CommentProvider commentProvider, JwtService jwtService) {
        this.commentDao = commentDao;
        this.commentProvider = commentProvider;
        this.jwtService = jwtService;
    }

    // 댓글 추가
    public PostCommentRes postComment(int userIndex, PostCommentReq postCommentReq) throws BaseException {
        if(commentProvider.isExistComment(userIndex, postCommentReq.getContentIndex()) == 1){
            throw new BaseException(POST_COMMENT_EXISTS);
        }

        int commentIndex = commentDao.postComment(userIndex, postCommentReq);
        return new PostCommentRes(commentIndex);
    }

    // 댓글 삭제
    public PatchCommentStatRes patchCommentStat(int userIndex, int contentIndex) throws BaseException {
        if(commentProvider.isExistComment(userIndex, contentIndex) == 0){
            throw new BaseException(PATCH_COMMENT_INVALID);
        }

        int commentIndex = commentDao.patchCommentStat(userIndex, contentIndex);
        return new PatchCommentStatRes(commentIndex);
    }

    // 좋아요 추가
    public PostCommentLikeRes postCommentLike(int userIndex, int commentIndex) throws BaseException {
        if(commentProvider.isExistsLikeOrUnlike(userIndex, commentIndex) == 1){
            throw new BaseException(POST_LIKE_OR_UNLIKE_EXISTS);
        }

        int likeIndex = commentDao.postCommentLike(userIndex, commentIndex);
        return new PostCommentLikeRes(likeIndex);
    }

    // 좋아요 삭제
    public PatchCommentLikeStatRes patchCommentLikeStat(int userIndex, int commentIndex) throws BaseException {
        if(commentProvider.isExistsLike(userIndex, commentIndex) == 0){
            throw new BaseException(POST_LIKE_INVALID);
        }

        int likeIndex = commentDao.patchCommentLikeStat(userIndex, commentIndex);
        return new PatchCommentLikeStatRes(likeIndex);
    }

    // 싫어요 추가
    public PostCommentUnlikeRes postCommentUnlike(int userIndex, int commentIndex) throws BaseException {
        if(commentProvider.isExistsLikeOrUnlike(userIndex, commentIndex) == 1){
            throw new BaseException(POST_LIKE_OR_UNLIKE_EXISTS);
        }

        int unlikeIndex = commentDao.postCommentUnlike(userIndex, commentIndex);
        return new PostCommentUnlikeRes(unlikeIndex);
    }

    // 싫어요 삭제
    public PatchCommentUnlikeStatRes patchCommentUnlikeStat(int userIndex, int commentIndex) throws BaseException {
        if(commentProvider.isExistsUnlike(userIndex, commentIndex) == 0){
            throw new BaseException(POST_UNLIKE_INVALID);
        }

        int unlikeIndex = commentDao.patchCommentUnlikeStat(userIndex, commentIndex);
        return new PatchCommentUnlikeStatRes(unlikeIndex);
    }

}
