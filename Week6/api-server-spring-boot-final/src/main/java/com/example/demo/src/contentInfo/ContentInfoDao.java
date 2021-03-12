package com.example.demo.src.contentInfo;


import com.example.demo.src.contentInfo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ContentInfoDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 웹툰 내용 조회.
    public GetContentInfoRes getContentInfo(int contentIndex){
        return this.jdbcTemplate.queryForObject("select title,\n" +
                "       content,\n" +
                "       musicUrl,\n" +
                "       IF(star is null, 0, truncate(star, 2)) as averageStar,\n" +
                "       writerComment,\n" +
                "       IF(heart is null, 0, heart) as heartSum,\n" +
                "       IF(commentCnt is null, 0, commentCnt) as commentSum\n" +
                "from (select contentIndex, title, content, musicUrl, writerComment\n" +
                "      from Content\n" +
                "      where contentIndex = ?) C\n" +
                "         left join (select contentIndex, avg(grade) as star\n" +
                "                    from Grade\n" +
                "                    where status = 'Y'\n" +
                "                    group by contentIndex) G on C.contentIndex = G.contentIndex\n" +
                "         left join (select contentIndex, count(heartIndex) as heart\n" +
                "                    from Heart\n" +
                "                    where status = 'Y'\n" +
                "                    group by contentIndex) H on H.contentIndex = C.contentIndex\n" +
                "         left join(select contentIndex, count(commentIndex) as commentCnt\n" +
                "                   from Comment\n" +
                "                   where status = 'Y'\n" +
                "                   group by contentIndex) CM on CM.contentIndex = C.contentIndex",
                (rs, rowNum) -> new GetContentInfoRes(
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("musicUrl"),
                        rs.getFloat("averageStar"),
                        rs.getString("writerComment"),
                        rs.getInt("heartSum"),
                        rs.getInt("commentSum")),
                contentIndex);
    }
}
