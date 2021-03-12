package com.example.demo.src.webtoon;


import com.example.demo.src.webtoon.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class WebtoonDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // 요일 별 웹툰 조회.
    public List<GetWebtoonRes> getWebtoonsDay(String serialDate){
        return this.jdbcTemplate.query("select imageUrl,\n" +
                        "       webtoonName,\n" +
                        "       IF(star is null, 0, truncate(star, 2))            as averageStar,\n" +
                        "       writer,\n" +
                        "       IF(DATEDIFF(NOW(), recentUploaded) = 0, 'UP', ' ') as isUploaded\n" +
                        "from (select webtoonIndex, imageUrl, webtoonName, writer\n" +
                        "      from Webtoon\n" +
                        "      where serialDate = ?\n" +
                        "        and isFinished = 'N'\n" +
                        "        and isChallenge = 'N') W\n" +
                        "         left join (select webtoonIndex, max(createdAt) as recentUploaded\n" +
                        "                    from Content\n" +
                        "                    where cookieValue = 0\n" +
                        "                    group by webtoonIndex) RC on RC.webtoonIndex = W.webtoonIndex\n" +
                        "         left join (select C.webtoonIndex, avg(grade) as star\n" +
                        "                    from Grade\n" +
                        "                             join Content C on Grade.contentIndex = C.contentIndex\n" +
                        "                    where Grade.status = 'Y'\n" +
                        "                    group by webtoonIndex) G on G.webtoonIndex = W.webtoonIndex\n" +
                        "order by star desc",
                (rs, rowNum) -> new GetWebtoonRes(
                        rs.getString("imageUrl"),
                        rs.getString("webtoonName"),
                        rs.getFloat("averageStar"),
                        rs.getString("writer"),
                        rs.getString("isUploaded")),
                serialDate);
    }

    // 신작 웹툰 조회.
    public List<GetWebtoonNewRes> getWebtoonsNew(){
        return this.jdbcTemplate.query("select imageUrl,\n" +
                        "       LEFT(serialDate, 1)                    as serialDate,\n" +
                        "       webtoonName,\n" +
                        "       IF(star is null, 0, truncate(star, 2)) as averageStar,\n" +
                        "       writer,\n" +
                        "       IF(DATEDIFF(NOW(),RecentUpload) = 0, 'UP', ' ') as isUploaded\n" +
                        "from (select Webtoon.webtoonIndex, imageUrl, serialDate, Webtoon.webtoonName, writer, max(C.createdAt) as RecentUpload\n" +
                        "      from Webtoon\n" +
                        "               join Content C on Webtoon.webtoonIndex = C.webtoonIndex\n" +
                        "      where isFinished = 'N'\n" +
                        "        and isChallenge = 'N'\n" +
                        "      group by Webtoon.webtoonIndex\n" +
                        "      having count(contentIndex) < 2) W\n" +
                        "         left join (select webtoonIndex, avg(grade) as star\n" +
                        "                    from Content\n" +
                        "                             join Grade on Content.contentIndex = Grade.contentIndex\n" +
                        "                    group by webtoonIndex) G on G.webtoonIndex = W.webtoonIndex\n" +
                        "order by star desc",
                (rs, rowNum) -> new GetWebtoonNewRes(
                        rs.getString("imageUrl"),
                        rs.getString("serialDate"),
                        rs.getString("webtoonName"),
                        rs.getFloat("averageStar"),
                        rs.getString("writer"),
                        rs.getString("isUploaded")));
    }

    // 완결 웹툰 조회.
    public List<GetWebtoonFinRes> getWebtoonsFinish(){
        return this.jdbcTemplate.query("select imageUrl,\n" +
                        "       webtoonName,\n" +
                        "       IF(star is null, 0, truncate(star, 2)) as averageStar,\n" +
                        "       writer\n" +
                        "from (select webtoonIndex, imageUrl, webtoonName, writer\n" +
                        "      from Webtoon\n" +
                        "      where isFinished = 'Y'\n" +
                        "        and isChallenge = 'N'\n" +
                        "        and status = 'Y') W\n" +
                        "         left join (select C.webtoonIndex, avg(grade) as star\n" +
                        "                    from Grade\n" +
                        "                             join Content C on Grade.contentIndex = C.contentIndex\n" +
                        "                    where Grade.status = 'Y'\n" +
                        "                    group by webtoonIndex) G on G.webtoonIndex = W.webtoonIndex\n" +
                        "order by star desc",
                (rs, rowNum) -> new GetWebtoonFinRes(
                        rs.getString("imageUrl"),
                        rs.getString("webtoonName"),
                        rs.getFloat("averageStar"),
                        rs.getString("writer")));
    }

    // 웹툰 정보 조회
    public WebtoonInfo getWebtoonInfo(int userIndex, int webtoonIndex){
        return this.jdbcTemplate.queryForObject("select imageUrl,\n" +
                "       IF(userIndex is null, 'N', 'Y')              as isInterested,\n" +
                "       IF(cnt is null, '0', CONCAT('관심 ', cnt, '개')) as interestedSum,\n" +
                "       webtoonName,\n" +
                "       writer,\n" +
                "       serialDate as serialDay,\n" +
                "       introduction,\n" +
                "       IF(countContent is null, '0개 미리보기', CONCAT(countContent,'개 미리보기')) as numberOfPreview\n" +
                "from (select webtoonIndex, imageUrl, webtoonName, writer, serialDate, introduction\n" +
                "      from Webtoon\n" +
                "      where webtoonIndex = ?) W\n" +
                "         left join (select webtoonIndex, count(myWebtoonIndex) as cnt\n" +
                "                    from MyWebtoon\n" +
                "                    group by webtoonIndex) I on W.webtoonIndex = I.webtoonIndex\n" +
                "         left join (select webtoonIndex, userIndex from MyWebtoon where userIndex = ?) My\n" +
                "                   on My.webtoonIndex = W.webtoonIndex\n" +
                "         left join (select webtoonIndex, count(contentIndex) as countContent\n" +
                "                    from Content\n" +
                "                    where cookieValue > 0\n" +
                "                    group by webtoonIndex) C on C.webtoonIndex = W.webtoonIndex",
                (rs, rowNum) -> new WebtoonInfo(
                        rs.getString("imageUrl"),
                        rs.getString("isInterested"),
                        rs.getString("interestedSum"),
                        rs.getString("webtoonName"),
                        rs.getString("writer"),
                        rs.getString("serialDay"),
                        rs.getString("introduction"),
                        rs.getString("numberOfPreview")),
                new Object[]{webtoonIndex, userIndex});
    }

    // 웹툰 콘텐츠 조회
    public List<Content> getContents(int userIndex, int webtoonIndex){
        return this.jdbcTemplate.query("select representativesImageUrl as contentImageUrl,\n" +
                "       title,\n" +
                "       IF(star is null, '0', truncate(star, 2)) as averageStar,\n" +
                "       DATE_FORMAT(createdAt, '%y.%m.%d')       as uploadedAt,\n" +
                "       IF(logIndex is null, 'N', 'Y')           as isReaded\n" +
                "from (select contentIndex, representativesImageUrl, title, createdAt\n" +
                "      from Content\n" +
                "      where webtoonIndex = ?) C\n" +
                "         left join (select contentIndex, avg(grade) as star\n" +
                "                    from Grade\n" +
                "                    where status = 'Y'\n" +
                "                    group by contentIndex) G on C.contentIndex = G.contentIndex\n" +
                "         left join (select contentIndex, logIndex\n" +
                "                    from Log\n" +
                "                    where userIndex = ?\n" +
                "                      and status = 'Y') L on L.contentIndex = C.contentIndex\n" +
                "order by C.contentIndex desc",
                (rs, rowNum) -> new Content(
                        rs.getString("contentImageUrl"),
                        rs.getString("title"),
                        rs.getFloat("averageStar"),
                        rs.getString("uploadedAt"),
                        rs.getString("isReaded")),
                new Object[]{webtoonIndex, userIndex});
    }

    // 별점 여부
    public int isAlreadyGraded(int userIndex, int contentIndex){
        return this.jdbcTemplate.queryForObject("select exists(select gradeIndex from Grade where userIndex=? and contentIndex=? and status='Y')",
                int.class,
                new Object[]{userIndex, contentIndex});
    }

    // 별점 추가
    public int postWebtoonStar(int userIndex, PostWebtoonStarReq postWebtoonStarReq){
        this.jdbcTemplate.update("INSERT INTO Grade (contentIndex, userIndex, grade) VALUES (?,?,?)",
                new Object[]{postWebtoonStarReq.getContentIndex(), userIndex, postWebtoonStarReq.getGrade()});
        return this.jdbcTemplate.queryForObject("select gradeIndex from Grade where userIndex=? and contentIndex=? and status='Y'",
                int.class,
                new Object[]{userIndex, postWebtoonStarReq.getContentIndex()});
    }

    // 하트 주었는지 여부
    public int isAlreadyHeart(int userIndex, int contentIndex){
        return this.jdbcTemplate.queryForObject("select exists(select heartIndex from Heart where userIndex=? and contentIndex=? and status='Y')",
                int.class,
                new Object[]{userIndex, contentIndex});
    }

    // 하트 추가
    public int postWebtoonHeart(int userIndex, int contentIndex){
        this.jdbcTemplate.update("INSERT INTO Heart (contentIndex, userIndex) VALUES (?,?)",
                new Object[]{contentIndex, userIndex});
        return this.jdbcTemplate.queryForObject("select heartIndex from Heart where userIndex=? and contentIndex=? and status='Y'",
                int.class,
                new Object[]{userIndex, contentIndex});
    }

    // 하트 유효성 확인
    public int isValidHeart(int userIndex, int contentIndex){
        return this.jdbcTemplate.queryForObject("select exists(select heartIndex from Heart where userIndex=? and contentIndex=? and status='Y')",
                int.class,
                new Object[]{userIndex, contentIndex});
    }

    // 하트 삭제
    public int patchWebtoonHeart(int userIndex, int contentIndex){
        int heartIndex = this.jdbcTemplate.queryForObject("select heartIndex from Heart where userIndex=? and contentIndex=? and status='Y'",
                int.class,
                new Object[]{userIndex, contentIndex});
        this.jdbcTemplate.update("UPDATE Heart SET status='N' where heartIndex=?",
                heartIndex);
        return heartIndex;
    }

    // 웹툰 내용 조회
    public GetWebtoonContentInfoRes getWebtoonContentInfo(int userIndex, int contentIndex){
        return this.jdbcTemplate.queryForObject("select title,\n" +
                        "       content,\n" +
                        "       IF(musicUrl is null, 'N', musicUrl) as musicUrl,\n" +
                        "       IF(star is null, 0, truncate(star,2)) as averageStar,\n" +
                        "       IF(MG.userIndex is null, 'N', 'Y') as isGraded,\n" +
                        "       writerComment,\n" +
                        "       writer,\n" +
                        "       IF(MI.userIndex is null, 'N', 'Y') as isInterested,\n" +
                        "       IF(heart is null, 0, heart) as heartSum,\n" +
                        "       IF(MH.userIndex is null, 'N', 'Y') as ishearted,\n" +
                        "       IF(commentCnt is null, 0, commentCnt) as commentSum\n" +
                        "from (select contentIndex, title, content, musicUrl, writerComment, webtoonIndex\n" +
                        "      from Content\n" +
                        "      where contentIndex = ?) C\n" +
                        "         left join (select contentIndex, avg(grade) as star\n" +
                        "                    from Grade\n" +
                        "                    where status = 'Y'\n" +
                        "                    group by contentIndex) G on C.contentIndex = G.contentIndex\n" +
                        "         join (select webtoonIndex, writer from Webtoon) W on W.webtoonIndex = C.webtoonIndex\n" +
                        "         left join (select contentIndex, count(heartIndex) as heart\n" +
                        "                    from Heart\n" +
                        "                    where status = 'Y'\n" +
                        "                    group by contentIndex) H on H.contentIndex = C.contentIndex\n" +
                        "         left join (select contentIndex, count(commentIndex) as commentCnt\n" +
                        "                    from Comment\n" +
                        "                    where status = 'Y'\n" +
                        "                    group by contentIndex) CM on CM.contentIndex = C.contentIndex\n" +
                        "         left join (select contentIndex, userIndex from Grade where userIndex = ?) MG\n" +
                        "                   on MG.contentIndex = C.contentIndex\n" +
                        "         left join(select webtoonIndex, userIndex from MyWebtoon where userIndex = ?) MI\n" +
                        "                  on MI.webtoonIndex = C.webtoonIndex\n" +
                        "         left join (select contentIndex, userIndex from Heart where userIndex = ?) MH\n" +
                        "                   on MH.contentIndex = C.contentIndex",
                (rs, rowNum) -> new GetWebtoonContentInfoRes(
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("musicUrl"),
                        rs.getFloat("averageStar"),
                        rs.getString("isGraded"),
                        rs.getString("writerComment"),
                        rs.getString("writer"),
                        rs.getString("isInterested"),
                        rs.getInt("heartSum"),
                        rs.getString("ishearted"),
                        rs.getInt("commentSum")),
                new Object[]{contentIndex, userIndex, userIndex, userIndex});
    }

}