package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 유효한 Idx인지 확인
    public int checkUserIdx(int userIndex){
        return this.jdbcTemplate.queryForObject("select exists(select userIndex from User where userIndex = ?)",
                int.class,
                userIndex);
    }

    // 사용자의 쿠키 조회
    public GetUserCookieRes getUserCookie(int userIndex){
        return this.jdbcTemplate.queryForObject("select CONCAT(userId, '님') as userName, IF(valueSum is null, 0, CONCAT(valueSum, '개')) as cookie\n" +
                        "from User\n" +
                        "         left join(select userIndex, sum(value) as valueSum\n" +
                        "              from CookieLog\n" +
                        "              group by userIndex) CL on User.userIndex = CL.userIndex\n" +
                        "where User.userIndex = ?",
                (rs, rowNum) -> new GetUserCookieRes(
                        rs.getString("userName"),
                        rs.getString("cookie")),
                userIndex);
    }

    //  아이디 중복 확인
    public int checkUserId(String userId){
        return this.jdbcTemplate.queryForObject("select exists(select userId from User where userId = ?)",
                int.class,
                userId);
    }

    // 닉네임 중복 확인
    public int checkUserNickname(String nickname){
        return this.jdbcTemplate.queryForObject("select exists(select nickname from User where nickname = ?)",
                int.class,
                nickname);
    }

    // 유저 생성
    public int createUser(PostUserJoinReq postUserJoinReq){
        this.jdbcTemplate.update("insert into User (userId, nickname, userPW, gender) VALUES (?,?,?,?)",
                new Object[]{postUserJoinReq.getUserId(), postUserJoinReq.getNickname(), postUserJoinReq.getUserPw(), postUserJoinReq.getGender()}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    // 로그인하기
    public int isValidUser(String userId, String userPw){
        return this.jdbcTemplate.queryForObject("select exists(select userId from User where userId = ? and userPw = ? and status = 'Y')",
                int.class,
                new Object[]{userId, userPw});
    }

    // 유저 아이디로 인덱스 얻기
    public int getUserIndexById(String userId){
        return this.jdbcTemplate.queryForObject("select userIndex from User where userId = ?",
                int.class,
                userId);
    }

    // 비빌번호 변경
    public String patchUserPw(int userIndex, String userPw){
        this.jdbcTemplate.update("UPDATE User SET userPw=? where userIndex = ?",
                userPw, userIndex);
        return this.jdbcTemplate.queryForObject("select userId from User where userIndex=?",
                String.class,
                userIndex);
    }

    // 회원 상태 변경 
    public String patchUserStat(int userIndex, String status){
        this.jdbcTemplate.update("UPDATE User SET status = ? where userIndex = ?",
                status, userIndex);
        return this.jdbcTemplate.queryForObject("select userId from User where userIndex=?",
                String.class,
                userIndex);
    }

}
