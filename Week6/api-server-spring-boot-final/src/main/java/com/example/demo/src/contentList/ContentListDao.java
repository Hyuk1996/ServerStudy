package com.example.demo.src.contentList;


import com.example.demo.src.contentList.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ContentListDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 웹툰 정보 조회.
    public GetWebtoonInfoRes getWebtoonInfo(int webtoonIndex){
        return this.jdbcTemplate.queryForObject("select imageUrl,\n" +
                "       IF(cnt is null, 0, cnt) as interest,\n" +
                "       webtoonName,\n" +
                "       writer,\n" +
                "       serialDate,\n" +
                "       introduction\n" +
                "from (select webtoonIndex, imageUrl, webtoonName, writer, serialDate, introduction\n" +
                "      from Webtoon\n" +
                "      where webtoonIndex = ?) W\n" +
                "         left join (select webtoonIndex, count(myWebtoonIndex) as cnt\n" +
                "                    from MyWebtoon\n" +
                "                    group by webtoonIndex) M on W.webtoonIndex = M.webtoonIndex",
                (rs, rowNum) -> new GetWebtoonInfoRes(
                        rs.getString("imageUrl"),
                        rs.getInt("interest"),
                        rs.getString("webtoonName"),
                        rs.getString("writer"),
                        rs.getString("serialDate"),
                        rs.getString("introduction")),
                webtoonIndex);
    }

    // 웹툰 회차 조회.
    public List<GetContentListRes> getContentList(int webtoonIndex){
        return this.jdbcTemplate.query("select representativesImageUrl,\n" +
                "       title,\n" +
                "       IF(star is null, '0', truncate(star, 2)) as averageStar,\n" +
                "       DATE_FORMAT(createdAt, '%y.%m.%d') as uploadedAt,\n" +
                "       cookieValue,\n" +
                "       IF(logIndex is null, 'N', 'Y') as hasSeen\n" +
                "from (select contentIndex, representativesImageUrl, title, createdAt, cookieValue\n" +
                "      from Content\n" +
                "      where webtoonIndex = ?) C\n" +
                "         left join (select contentIndex, avg(grade) as star\n" +
                "                    from Grade\n" +
                "                    where status = 'Y'\n" +
                "                    group by contentIndex) G on C.contentIndex = G.contentIndex\n" +
                "         left join (select contentIndex, logIndex\n" +
                "                    from Log\n" +
                "                    where userIndex = 1\n" +
                "                      and status = 'Y') L on L.contentIndex = C.contentIndex\n" +
                "order by C.contentIndex desc",
                (rs, rowNum) -> new GetContentListRes(
                        rs.getString("representativesImageUrl"),
                        rs.getString("title"),
                        rs.getFloat("averageStar"),
                        rs.getString("uploadedAt"),
                        rs.getInt("cookieValue"),
                        rs.getString("hasSeen")),
                webtoonIndex);
    }

}
