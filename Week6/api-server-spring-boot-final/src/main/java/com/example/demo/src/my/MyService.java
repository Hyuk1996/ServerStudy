package com.example.demo.src.my;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.my.model.*;
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
public class MyService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MyDao myDao;
    private final MyProvider myProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public MyService(MyDao myDao, MyProvider myProvider, JwtService jwtService) {
        this.myDao = myDao;
        this.myProvider = myProvider;
        this.jwtService = jwtService;
    }

    // 관심 웹툰 추가
    public PostMyInterestRes postMyInterest(int userIndex, int webtoonIndex) throws BaseException {
        // 의미적 validation
        if(myProvider.isValidWebtoonIdx(webtoonIndex) == 0){
            throw new BaseException(INVALID_WEBTOON_IDX);
        }

        myDao.postMyInterest(userIndex, webtoonIndex);
        return new PostMyInterestRes(userIndex,webtoonIndex);
    }

    // 관심 웹툰 삭제
    public PatchMyInterestRes patchMyInterest(int userIndex, int webtoonIndex) throws BaseException {
        // validation
        if(myProvider.isValidWebtoonIdx(webtoonIndex) == 0){
            throw new BaseException(INVALID_WEBTOON_IDX);
        }

        myDao.patchMyInterest(userIndex, webtoonIndex);
        return new PatchMyInterestRes(webtoonIndex);
    }

    // 알람 켜기
    public PatchMyAlarmRes patchMyAlarmOn(int userIndex, int webtoonIndex){
        int myWebtoonIndex = myDao.patchMyAlarmOn(userIndex, webtoonIndex);
        return new PatchMyAlarmRes(myWebtoonIndex);
    }

    // 알람 끄기
    public PatchMyAlarmRes patchMyAlarmOff(int userIndex, int webtoonIndex){
        int myWebtoonIndex = myDao.patchMyAlarmOff(userIndex, webtoonIndex);
        return new PatchMyAlarmRes(myWebtoonIndex);
    }

    // 읽은 기록 추가
    public PostMyLogRes postMyLog(int userIndex, int contentIndex) throws BaseException {
        if(myProvider.isReaded(userIndex, contentIndex) == 1){
            throw new BaseException(POST_LOG_CONTENT_EXISTS);
        }

        int logIndex = myDao.postMyLog(userIndex, contentIndex);
        return new PostMyLogRes(logIndex);
    }

    // 임시 저장 콘텐츠 추가
    public PostMyTempContentRes postMyTempContent(int userIndex, int contentIndex) throws BaseException {
        if(myProvider.isStoraged(userIndex, contentIndex) == 1){
            throw new BaseException(POST_TEMP_CONTENT_EXISTS);
        }

        int tempContentIndex = myDao.postMyTempContent(userIndex,contentIndex);
        return new PostMyTempContentRes(tempContentIndex);
    }

    // 임시 저장 콘텐츠 삭제
    public PatchMyTempContentRes patchMyTempContent(int userIndex, int contentIndex) throws BaseException {
        if(myProvider.isValidLog(userIndex, contentIndex) == 0){
            throw new BaseException(PATCH_TEMP_CONTENT_INVALID);
        }

        int tempContentIndex = myDao.patchMyTempContent(userIndex, contentIndex);
        return new PatchMyTempContentRes(tempContentIndex);
    }

    // 쿠키 구입
    public PostMyCookieBuyRes postMyCookieBuy(int userIndex, int cookie){
        int cookieLogIndex = myDao.postMyCookieBuy(userIndex, cookie);
        return new PostMyCookieBuyRes(cookieLogIndex);
    }

    // 쿠키 사용
    public PostMyCookieUseRes postMyCookieUse(int userIndex, PostMyCookieUseReq postMyCookieUseReq) throws BaseException {
        if(myProvider.currentCookie(userIndex) < postMyCookieUseReq.getCookie()){
            throw new BaseException(POST_NO_COOKIE);
        }

        int cookieLogIndex = myDao.postMyCookieUse(userIndex, postMyCookieUseReq);
        return new PostMyCookieUseRes(cookieLogIndex);
    }

}
