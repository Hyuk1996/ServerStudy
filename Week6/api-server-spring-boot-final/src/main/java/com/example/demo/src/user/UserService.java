package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
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
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    // 회원가입
    public PostUserJoinRes postUserJoin(PostUserJoinReq postUserJoinReq) throws BaseException {
        //의미적 validation
        if(userProvider.checkUserId(postUserJoinReq.getUserId()) ==1){
            throw new BaseException(POST_USERS_EXISTS_USER_ID);
        }
        if(userProvider.checkUserNickname(postUserJoinReq.getNickname()) == 1){
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }

        /*String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserJoinReq.getUserPw());
            postUserJoinReq.setUserPw(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }*/

        int userIndex = userDao.createUser(postUserJoinReq);
        return new PostUserJoinRes(userIndex);
    }

    // 비밀번호 변경
    public PatchUserPwRes patchUserPw(int userIndex, String userPw) {
        String userId = userDao.patchUserPw(userIndex, userPw);
        return new PatchUserPwRes(userId);
    }

    // 회원 상태 변경 
    public PatchUserStatRes patchUserStat(int userIndex, String status) {
        String userId = userDao.patchUserStat(userIndex, status);
        return new PatchUserStatRes(userId);
    }
}
