package com.example.demo.src.my;


import com.example.demo.src.my.model.*;
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
public class MyProvider {

    private final MyDao myDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public MyProvider(MyDao myDao, JwtService jwtService) {
        this.myDao = myDao;
        this.jwtService = jwtService;
    }

    // 관심 웹툰 조회.
    public GetMyInterestRes getMyInterests(int userIndex){
        int numOfInterest = myDao.getNumOfInterest(userIndex);

        List<Interest> interests = myDao.getMyInterests(userIndex);
        return new GetMyInterestRes(numOfInterest, interests);
    }

    // 최근 본 웹툰 조회.
    public GetMyRecentWebtoonRes getMyRecentWebtoons(int userIndex){
        int numOfRecentWebtoon = myDao.getNumOfRecentWebtoon(userIndex);

        List<RecentWebtoon> recentWebtoons = myDao.getMyRecentWebtoons(userIndex);
        return new GetMyRecentWebtoonRes(numOfRecentWebtoon, recentWebtoons);
    }

    // 임시 저장 웹툰 조회.
    public GetMyTemWebtoonRes getMyTemWebtoons(int userIndex){
        int numOfTempWebtoon = myDao.getNumOfTempWebtoon(userIndex);

        List<TempWebtoon> tempWebtoons = myDao.getMyTemWebtoons(userIndex);
        return new GetMyTemWebtoonRes(numOfTempWebtoon, tempWebtoons);
    }

    // 구입 쿠키내역 조회
    public GetMyCookieBuyRes getMyCookieBuy(int userIndex){
        String currentCookie = myDao.getCurrentCookie(userIndex);

        List<Buy> buys = myDao.getBuyList(userIndex);
        return new GetMyCookieBuyRes(currentCookie, buys);
    }

    // 쿠키 사용 내역 조회
    public GetMyCookieUseRes getMyCookieUse(int userIndex){
        String currentCookie = myDao.getCurrentCookie(userIndex);

        List<Use> uses = myDao.getUseList(userIndex);
        return new GetMyCookieUseRes(currentCookie, uses);
    }

    // 웹툰 인덱스 유효성 확인
    public int isValidWebtoonIdx(int webtoonIndex){
        return myDao.isValidWebtoonIdx(webtoonIndex);
    }

    // 콘텐츠를 이미 읽었는지 확인
    public int isReaded(int userIndex, int contentIndex){
        return myDao.isReaded(userIndex, contentIndex);
    }

    // 콘텐츠 저장 여부 확인
    public int isStoraged(int userIndex, int contentIndex){
        return myDao.isStoraged(userIndex, contentIndex);
    }

    // 임시 저장 콘텐츠 존재 확인
    public int isValidLog(int userIndex, int contentIndex){
        return myDao.isValidLog(userIndex,contentIndex);
    }

    // 남은 쿠키 양 조회
    public int currentCookie(int userIndex){
        return myDao.CurrentCookie(userIndex);
    }
}
