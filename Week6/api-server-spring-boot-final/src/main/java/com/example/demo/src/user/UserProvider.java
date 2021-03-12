package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.example.demo.config.BaseException;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    // 유저의 쿠키 조회
    public GetUserCookieRes getUserCookie(int userIndex) throws BaseException {
        // 의미적 validation
        if(userDao.checkUserIdx(userIndex) == 0){
            throw new BaseException(GET_USERS_INVALID_IDX);
        }

        GetUserCookieRes getUserCookieRes = userDao.getUserCookie(userIndex);
        return getUserCookieRes;
    }

    // 아이디 중복 확인
    public int checkUserId(String userId){
        return userDao.checkUserId(userId);
    }

    // 닉네임 중복 확인 
    public int checkUserNickname(String nickname){return userDao.checkUserNickname(nickname);}

    // 로그인
    public PostUserLoginRes postUserLogin(String userId, String userPw) throws BaseException {
        // 의미적 validation
        if(userDao.isValidUser(userId, userPw) == 0){
            throw new BaseException(GET_USERS_INVALID_USER);
        }

        int userIndex = userDao.getUserIndexById(userId);
        String jwt = jwtService.createJwt(userIndex);
        return new PostUserLoginRes(userIndex, jwt);
    }


}
