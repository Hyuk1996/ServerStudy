package com.example.demo.src.webtoon;


import com.example.demo.src.webtoon.model.*;
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
public class WebtoonProvider {

    private final WebtoonDao webtoonDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public WebtoonProvider(WebtoonDao webtoonDao, JwtService jwtService) {
        this.webtoonDao = webtoonDao;
        this.jwtService = jwtService;
    }


    // 요일 별 웹툰 조회.
    public List<GetWebtoonRes> getWebtoonsDay(String serialDate){
        List<GetWebtoonRes> getWebtoonsRes = webtoonDao.getWebtoonsDay(serialDate);
        return getWebtoonsRes;
    }

    // 신작 웹툰 조회.
    public List<GetWebtoonNewRes> getWebtoonsNew(){
        List<GetWebtoonNewRes> getWebtoonsNewRes = webtoonDao.getWebtoonsNew();
        return getWebtoonsNewRes;
    }

    // 완결 웹툰 조회.
    public List<GetWebtoonFinRes> getWebtoonsFinish(){
        List<GetWebtoonFinRes> getWebtoonsFinRes = webtoonDao.getWebtoonsFinish();
        return getWebtoonsFinRes;
    }

    // 웹툰 콘텐츠 조회.
    public GetWebtoonContentsRes getWebtoonContents(int userIndex, int webtoonIndex){
        WebtoonInfo webtoonInfo = webtoonDao.getWebtoonInfo(userIndex, webtoonIndex);

        List<Content> contents = webtoonDao.getContents(userIndex, webtoonIndex);
        return new GetWebtoonContentsRes(webtoonInfo, contents);
    }

    // 별점 여부
    public int isAlreadyGraded(int userIndex, int contentIndex){
        return webtoonDao.isAlreadyGraded(userIndex, contentIndex);
    }

    // 하트 주었는지 여부
    public int isAlreadyHeart(int userIndex, int contentIndex){
        return webtoonDao.isAlreadyHeart(userIndex, contentIndex);
    }

    // 유효한 하트인지 확인
    public int isValidHeart(int userIndex, int contentIndex){
        return webtoonDao.isValidHeart(userIndex, contentIndex);
    }

    // 콘텐츠 내용 조회
    public GetWebtoonContentInfoRes getWebtoonContentInfo(int userIndex, int contentIndex){
        GetWebtoonContentInfoRes getWebtoonContentInfoRes = webtoonDao.getWebtoonContentInfo(userIndex, contentIndex);
        return getWebtoonContentInfoRes;
    }

}
