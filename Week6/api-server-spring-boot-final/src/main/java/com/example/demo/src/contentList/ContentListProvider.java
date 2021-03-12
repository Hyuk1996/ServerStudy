package com.example.demo.src.contentList;


import com.example.demo.src.contentList.model.*;
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
public class ContentListProvider {

    private final ContentListDao contentListDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public ContentListProvider(ContentListDao contentListDao, JwtService jwtService) {
        this.contentListDao = contentListDao;
        this.jwtService = jwtService;
    }

    // 웹툰 정보 조회.
    public GetWebtoonInfoRes getWebtoonInfo(int webtoonIndex){
        GetWebtoonInfoRes getWebtoonInfoRes = contentListDao.getWebtoonInfo(webtoonIndex);
        return getWebtoonInfoRes;
    }

    // 웹툰 회차 조회.
    public List<GetContentListRes> getContentList(int webtoonIndex){
        List<GetContentListRes> getContentListRes = contentListDao.getContentList(webtoonIndex);
        return getContentListRes;
    }

}
