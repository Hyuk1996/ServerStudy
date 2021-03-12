package com.example.demo.src.my;


import com.example.demo.src.my.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 관심 웹툰 개수 조회
    public int getNumOfInterest(int userIndex){
        return this.jdbcTemplate.queryForObject("select count(myWebtoonIndex) from MyWebtoon where userIndex=? and status = 'Y'",
                int.class,
                userIndex);
    }

    // 관심 웹툰 조회.
    public List<Interest> getMyInterests(int userIndex){
        return this.jdbcTemplate.query("select imageUrl,\n" +
                "       webtoonName,\n" +
                "       DATE_FORMAT(uploadedAt, '%y.%m.%d') as uploadedAt,\n" +
                "       isAlarm as isAlarmed\n" +
                "from (select webtoonIndex, isAlarm\n" +
                "      from MyWebtoon\n" +
                "      where userIndex = ?\n" +
                "        and status = 'Y') MW\n" +
                "         join (select webtoonIndex, imageUrl, webtoonName\n" +
                "               from Webtoon) W on W.webtoonIndex = MW.webtoonIndex\n" +
                "         join (select webtoonIndex, max(createdAt) as uploadedAt\n" +
                "               from Content\n" +
                "               group by webtoonIndex) C on C.webtoonIndex = MW.webtoonIndex\n" +
                "order by uploadedAt",
                (rs, rowNum) -> new Interest(
                        rs.getString("imageUrl"),
                        rs.getString("webtoonName"),
                        rs.getString("uploadedAt"),
                        rs.getString("isAlarmed")),
                userIndex);
    }

    // 최근 본 웹툰 개수 조회
    public int getNumOfRecentWebtoon(int userIndex){
        return this.jdbcTemplate.queryForObject("select count(logIndex) from Log where userIndex=? and status='Y'",
                int.class,
                userIndex);
    }

    // 최근 본 웹툰 조회.
    public List<RecentWebtoon> getMyRecentWebtoons(int userIndex){
        return this.jdbcTemplate.query("select imageUrl\n" +
                "     , webtoonName\n" +
                "     , IF(DATEDIFF(NOW(), createdAt) = 0, '오늘', CONCAT(\n" +
                "        DATEDIFF(NOW(), createdAt)\n" +
                "    , '일전'\n" +
                "    )) as daysAgo\n" +
                "     , CONCAT(\n" +
                "        episodeNum\n" +
                "    , '화'\n" +
                "    )  as episodeNum\n" +
                "from (select createdAt, webtoonIndex, episodeNum\n" +
                "      from (select logIndex, contentIndex, createdAt\n" +
                "            from Log\n" +
                "            where userIndex = ?\n" +
                "              and status = 'Y') L\n" +
                "               join (select contentIndex, webtoonIndex, episodeNum\n" +
                "                     from Content) C on C.contentIndex = L.contentIndex\n" +
                "      where logIndex in (select max(logIndex)\n" +
                "                         from (select logIndex, contentIndex\n" +
                "                               from Log\n" +
                "                               where status = 'Y') A\n" +
                "                                  join (select contentIndex, webtoonIndex\n" +
                "                                        from Content) B on A.contentIndex = B.contentIndex\n" +
                "                         group by webtoonIndex)) LC\n" +
                "         join (select webtoonIndex, imageUrl, webtoonName\n" +
                "               from Webtoon) W on W.webtoonIndex = LC.webtoonIndex\n" +
                "order by createdAt desc",
                (rs, rowNum) -> new RecentWebtoon(
                        rs.getString("imageUrl"),
                        rs.getString("webtoonName"),
                        rs.getString("daysAgo"),
                        rs.getString("episodeNum")),
                userIndex);
    }

    // 임시 저장 웹툰 개수
    public int getNumOfTempWebtoon(int userIndex){
        return this.jdbcTemplate.queryForObject("select count(storageIndex) from TemporaryStorage where userIndex=? and status='Y'",
                int.class,
                userIndex);
    }

    // 임시 저장 웹툰 조회.
    public List<TempWebtoon> getMyTemWebtoons(int userIndex){
        return this.jdbcTemplate.query("select imageUrl, webtoonName, writer\n" +
                "from (select webtoonIndex\n" +
                "      from (select contentIndex\n" +
                "            from TemporaryStorage\n" +
                "            where userIndex = ?\n" +
                "              and status = 'Y') TS\n" +
                "               join (select contentIndex, webtoonIndex\n" +
                "                     from Content) C on C.contentIndex = TS.contentIndex\n" +
                "      group by webtoonIndex) TC\n" +
                "         join (select webtoonIndex, imageUrl, webtoonName, writer\n" +
                "               from Webtoon) W on W.webtoonIndex = TC.webtoonIndex",
                (rs, rowNum) -> new TempWebtoon(
                        rs.getString("imageUrl"),
                        rs.getString("webtoonName"),
                        rs.getString("writer")),
                userIndex);
    }

    // 현재 남은 쿠키
    public String getCurrentCookie(int userIndex){
        return this.jdbcTemplate.queryForObject("select CONCAT(IF(sum(value) is null, 0, sum(value)), '개') as currentCookie\n" +
                "from CookieLog\n" +
                "where userIndex = ? and status = 'Y'",
                String.class,
                userIndex);
    }

    // 현재 남은 쿠키
    public int CurrentCookie(int userIndex){
        return this.jdbcTemplate.queryForObject("select IF(sum(value) is null, 0, sum(value)) from CookieLog where userIndex=? and status='Y'",
                int.class,
                userIndex);
    }

    // 쿠키 구입 내역
    public List<Buy> getBuyList(int userIndex){
        return this.jdbcTemplate.query("select DATE_FORMAT(createdAt, '%Y-%m-%d') as buyDay, CONCAT(value,'개') as cookie\n" +
                        "from CookieLog\n" +
                        "where userIndex = ? and value > 0\n" +
                        "order by createdAt desc",
                (rs, rowNum) -> new Buy(
                        rs.getString("buyDay"),
                        rs.getString("cookie")),
                userIndex);
    }

    // 쿠키 사용 내역
    public List<Use> getUseList(int userIndex){
        return this.jdbcTemplate.query("select DATE_FORMAT(CookieLog.createdAt, '%Y-%m-%d') as useDay, webtoonName, title, CONCAT(abs(value), '개') as cookie\n" +
                "from CookieLog\n" +
                "         join Content on CookieLog.contentIndex = Content.contentIndex\n" +
                "         join Webtoon on Webtoon.webtoonIndex = Content.webtoonIndex\n" +
                "where userIndex = ?",
                (rs, rowNum) -> new Use(
                        rs.getString("useDay"),
                        rs.getString("webtoonName"),
                        rs.getString("title"),
                        rs.getString("cookie")),
                userIndex);
    }

    // 웹툰 인덱스 유효성 확인
    public int isValidWebtoonIdx(int webtoonIndex){
        return this.jdbcTemplate.queryForObject("select exists(select webtoonIndex from Webtoon where webtoonIndex = ?)",
                int.class,
                webtoonIndex);
    }

    // 관심 웹툰 추가
    public void postMyInterest(int userIndex, int webtoonIndex){
        this.jdbcTemplate.update("INSERT INTO MyWebtoon (webtoonIndex, userIndex) VALUES (?,?)",
                new Object[]{webtoonIndex, userIndex});
    }

    // 관심 웹툰 삭제
    public void patchMyInterest(int userIndex, int webtoonIndex){
        this.jdbcTemplate.update("UPDATE MyWebtoon SET status = 'N' where userIndex = ? and webtoonIndex =? and status = 'Y'",
                new Object[]{userIndex, webtoonIndex});
    }

    // 알람 켜기
    public int patchMyAlarmOn(int userIndex, int webtoonIndex){
        this.jdbcTemplate.update("UPDATE MyWebtoon set isAlarm = 'Y' where userIndex = ? and webtoonIndex = ? and status = 'Y'",
                new Object[]{userIndex, webtoonIndex});
        return this.jdbcTemplate.queryForObject("select myWebtoonIndex from MyWebtoon where userIndex =? and webtoonIndex=? and status ='Y'",
                int.class,
                new Object[]{userIndex, webtoonIndex});
    }

    // 알람 끄기
    public int patchMyAlarmOff(int userIndex, int webtoonIndex){
        this.jdbcTemplate.update("UPDATE MyWebtoon set isAlarm = 'N' where userIndex = ? and webtoonIndex = ? and status = 'Y'",
                new Object[]{userIndex, webtoonIndex});
        return this.jdbcTemplate.queryForObject("select myWebtoonIndex from MyWebtoon where userIndex =? and webtoonIndex=? and status ='Y'",
                int.class,
                new Object[]{userIndex, webtoonIndex});
    }

    // 이미 읽은 콘텐츠 인지 확인
    public int isReaded(int userIndex, int contentIndex){
        return this.jdbcTemplate.queryForObject("select exists(select logIndex from Log where userIndex = ? and contentIndex = ? and status ='Y')",
                int.class,
                new Object[]{userIndex, contentIndex});
    }

    // 읽은 기록 추가
    public int postMyLog(int userIndex, int contentIndex){
        this.jdbcTemplate.update("INSERT INTO Log (contentIndex, userIndex) VALUES  (?,?)",
                new Object[]{contentIndex, userIndex});
        return this.jdbcTemplate.queryForObject("select logIndex from Log where userIndex=? and contentIndex=? and status='Y'",
                int.class,
                new Object[]{userIndex, contentIndex});
    }

    // 이미 저장 콘텐츠 인지 확인
    public int isStoraged(int userIndex, int contentIndex){
        return this.jdbcTemplate.queryForObject("select exists(select storageIndex from TemporaryStorage where userIndex = ? and contentIndex = ? and status = 'Y')",
                int.class,
                new Object[]{userIndex, contentIndex});
    }

    // 임시 저장 콘텐츠 추가
    public int postMyTempContent(int userIndex, int contentIndex){
        this.jdbcTemplate.update("INSERT INTO TemporaryStorage (contentIndex, userIndex) VALUES (?,?)",
                new Object[]{contentIndex, userIndex});
        return this.jdbcTemplate.queryForObject("select storageIndex from TemporaryStorage where userIndex=? and contentIndex=? and status='Y'",
                int.class,
                new Object[]{userIndex, contentIndex});
    }

    // 임시 저장 콘텐츠 존재 확인
    public int isValidLog(int userIndex, int contentIndex){
        return this.jdbcTemplate.queryForObject("select exists(select storageIndex from TemporaryStorage where userIndex=? and contentIndex=? and status='Y')",
                int.class,
                new Object[]{userIndex, contentIndex});
    }

    // 임시 저장 콘텐츠 삭제
    public int patchMyTempContent(int userIndex, int contentIndex){
        int tempContentIndex = this.jdbcTemplate.queryForObject("select storageIndex from TemporaryStorage where userIndex=? and contentIndex=? and status='Y'",
                int.class,
                new Object[]{userIndex, contentIndex});
        this.jdbcTemplate.update("UPDATE TemporaryStorage SET status = 'N' where userIndex=? and contentIndex=? and status='Y'",
                new Object[]{userIndex, contentIndex});
        return tempContentIndex;
    }

    // 쿠키 구입
    public int postMyCookieBuy(int userIndex, int cookie){
        this.jdbcTemplate.update("INSERT INTO CookieLog (userIndex, value) VALUES (?,?)",
                new Object[]{userIndex, cookie});
        return this.jdbcTemplate.queryForObject("select last_insert_id()", int.class);
    }

    // 쿠키 사용
    public int postMyCookieUse(int userIndex, PostMyCookieUseReq postMyCookieUseReq){
        int cookie = postMyCookieUseReq.getCookie() * (-1);
        this.jdbcTemplate.update("INSERT INTO CookieLog (userIndex, value, contentIndex) VALUES (?,?,?)",
                new Object[]{userIndex, cookie, postMyCookieUseReq.getContentIndex()});
        return this.jdbcTemplate.queryForObject("select last_insert_id()", int.class);
    }

}
