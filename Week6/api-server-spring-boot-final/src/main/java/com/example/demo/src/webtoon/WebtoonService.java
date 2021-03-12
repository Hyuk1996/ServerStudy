package com.example.demo.src.webtoon;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.webtoon.model.*;
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
public class WebtoonService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final WebtoonDao webtoonDao;
    private final WebtoonProvider webtoonProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public WebtoonService(WebtoonDao webtoonDao, WebtoonProvider webtoonProvider, JwtService jwtService) {
        this.webtoonDao = webtoonDao;
        this.webtoonProvider = webtoonProvider;
        this.jwtService = jwtService;

    }

   // 별점 주기
    public PostWebtoonStarRes postWebtoonStar(int userIndex, PostWebtoonStarReq postWebtoonStarReq) throws BaseException {
        if(webtoonProvider.isAlreadyGraded(userIndex, postWebtoonStarReq.getContentIndex()) == 1){
            throw new BaseException(POST_STAR_EXISTS);
        }

        int gradeIndex = webtoonDao.postWebtoonStar(userIndex, postWebtoonStarReq);
        return new PostWebtoonStarRes(gradeIndex);
    }

    // 하트 추가
    public PostWebtoonHeartRes postWebtoonHeart(int userIndex, int contentIndex) throws BaseException {
        if(webtoonProvider.isAlreadyHeart(userIndex, contentIndex) == 1){
            throw new BaseException(POST_HEART_EXISTS);
        }

        int heartIndex = webtoonDao.postWebtoonHeart(userIndex,contentIndex);
        return new PostWebtoonHeartRes(heartIndex);
    }

    // 하트 삭제
    public PatchWebtoonHeartRes patchWebtoonHeart(int userIndex, int contentIndex) throws BaseException {
        if(webtoonProvider.isValidHeart(userIndex, contentIndex) == 0){
            throw new BaseException(POST_HEART_INVALID);
        }

        int heartIndex = webtoonDao.patchWebtoonHeart(userIndex, contentIndex);
        return new PatchWebtoonHeartRes(heartIndex);
    }

}
