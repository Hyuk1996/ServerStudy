package com.example.demo.src.contentInfo;


import com.example.demo.src.contentInfo.model.*;
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
public class ContentInfoProvider {

    private final ContentInfoDao contentInfoDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public ContentInfoProvider(ContentInfoDao contentInfoDao, JwtService jwtService) {
        this.contentInfoDao = contentInfoDao;
        this.jwtService = jwtService;
    }

    // 웹툰 내용 조회.
    public GetContentInfoRes getContentInfo(int contentIndex){
        GetContentInfoRes getContentInfoRes = contentInfoDao.getContentInfo(contentIndex);
        return getContentInfoRes;
    }

}
