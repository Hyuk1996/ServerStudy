package com.example.demo.src.comment;


import com.example.demo.src.comment.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

//Provider : Read의 비즈니스 로직 처리
@Service
public class CommentProvider {

    private final CommentDao commentDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public CommentProvider(CommentDao commentDao, JwtService jwtService) {
        this.commentDao = commentDao;
        this.jwtService = jwtService;
    }

    // 댓글 조회.
    public GetCommentRes getComments(int userIndex, int contentIndex){
        int numOfComment = commentDao.getNumOfComment(contentIndex);

        List<Comment> comments = commentDao.getComments(userIndex, contentIndex);
        return new GetCommentRes(numOfComment, comments);
    }

    // 베스트 댓글 조회
    public GetCommentBestRes getCommentsBest(int userIndex, int contentIndex){
        int numOfComment = commentDao.getNumOfComment(contentIndex);

        List<BestComment> bestComments = commentDao.getCommentsBest(userIndex, contentIndex);
        return new GetCommentBestRes(numOfComment, bestComments);
    }

    // 댓글 존재하는지 확인
    public int isExistComment(int userIndex, int contentIndex){
        return commentDao.isExistComment(userIndex, contentIndex);
    }

    // 좋아요나 싫어요 눌렀는지 확인
    public int isExistsLikeOrUnlike(int userIndex, int commentIndex){
        return commentDao.isExistsLikeOrUnlike(userIndex, commentIndex);
    }

    // 좋아요 눌렀는지 확인
    public int isExistsLike(int userIndex, int commentIndex){
        return commentDao.isExistsLike(userIndex, commentIndex);
    }

    // 싫어요 눌렀는지 확인
    public int isExistsUnlike(int userIndex, int commentIndex){
        return commentDao.isExistsUnlike(userIndex, commentIndex);
    }

}
