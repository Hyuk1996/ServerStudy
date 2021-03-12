package com.example.demo.src.comment;


import com.example.demo.src.comment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CommentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 댓글 개수 조회
    public int getNumOfComment(int contentIndex){
        return this.jdbcTemplate.queryForObject("select IF(count(commentIndex) is null, 0, count(commentIndex))\n" +
                "from Comment\n" +
                "where contentIndex = ?",
                int.class,
                contentIndex);
    }

    // 댓글 조회
    public List<Comment> getComments(int userIndex, int contentIndex){
        return this.jdbcTemplate.query("select nickname,\n" +
                "       CONCAT(\n" +
                "               SUBSTRING(userId, 1, 4)\n" +
                "           , LPAD('*', CHAR_LENGTH(userId) - 4, '*')\n" +
                "           )                                 as userId,\n" +
                "       DATE_FORMAT(createdAt, '%Y.%m.%d %T') as uploadedAt,\n" +
                "       commentContent as content,\n" +
                "       IF(likeSum is null, 0, likeSum) as likeSum,\n" +
                "       IF(ML.userIndex is null, 'N', 'Y') as isLiked,\n" +
                "       IF(unlikeSum is null, 0, unlikeSum) as unlikeSum,\n" +
                "       IF(MUL.userIndex is null, 'N', 'Y') as isUnliked\n" +
                "from (select userIndex, commentIndex, commentContent, createdAt\n" +
                "      from Comment\n" +
                "      where status = 'Y'\n" +
                "        and contentIndex = ?) C\n" +
                "         join (select userIndex, userId, nickname\n" +
                "               from User\n" +
                "               where status = 'Y') U on C.userIndex = U.userIndex\n" +
                "         left join (select commentIndex, count(likeIndex) as likeSum\n" +
                "                    from LikeOrUnlike\n" +
                "                    where good = 'Y'\n" +
                "                      and status = 'Y'\n" +
                "                    group by commentIndex) L on L.commentIndex = C.commentIndex\n" +
                "         left join (select commentIndex, count(likeIndex) as unlikeSum\n" +
                "                    from LikeOrUnlike\n" +
                "                    where good = 'N'\n" +
                "                      and status = 'Y'\n" +
                "                    group by commentIndex) UL on UL.commentIndex = C.commentIndex\n" +
                "         left join (select commentIndex, userIndex\n" +
                "                    from LikeOrUnlike\n" +
                "                    where status = 'Y'\n" +
                "                      and good = 'Y'\n" +
                "                      and userIndex = ?) ML on ML.commentIndex = C.commentIndex\n" +
                "         left join (select commentIndex, userIndex\n" +
                "                    from LikeOrUnlike\n" +
                "                    where status = 'Y'\n" +
                "                      and good = 'N'\n" +
                "                      and userIndex = ?) MUL on MUL.commentIndex = C.commentIndex\n" +
                "order by createdAt desc",
                (rs, rowNum) -> new Comment(
                        rs.getString("nickname"),
                        rs.getString("userId"),
                        rs.getString("uploadedAt"),
                        rs.getString("content"),
                        rs.getInt("likeSum"),
                        rs.getString("isLiked"),
                        rs.getInt("unlikeSum"),
                        rs.getString("isUnliked")),
                new Object[]{contentIndex,userIndex,userIndex});
    }

    // 베스트 댓글 조회
    public List<BestComment> getCommentsBest(int userIndex, int contentIndex){
        return this.jdbcTemplate.query("select nickname,\n" +
                        "       CONCAT(\n" +
                        "               SUBSTRING(userId, 1, 4)\n" +
                        "           , LPAD('*', CHAR_LENGTH(userId) - 4, '*')\n" +
                        "           )                                 as userId,\n" +
                        "       DATE_FORMAT(createdAt, '%Y.%m.%d %T') as uploadedAt,\n" +
                        "       commentContent as content,\n" +
                        "       IF(likeSum is null, 0, likeSum) as likeSum,\n" +
                        "       IF(ML.userIndex is null, 'N', 'Y') as isLiked,\n" +
                        "       IF(unlikeSum is null, 0, unlikeSum) as unlikeSum,\n" +
                        "       IF(MUL.userIndex is null, 'N', 'Y') as isUnliked,\n" +
                        "       IF(likeSum > 1, 'BEST', '') as best\n" +
                        "from (select userIndex, commentIndex, commentContent, createdAt\n" +
                        "      from Comment\n" +
                        "      where status = 'Y'\n" +
                        "        and contentIndex = ?) C\n" +
                        "         join (select userIndex, userId, nickname\n" +
                        "               from User\n" +
                        "               where status = 'Y') U on C.userIndex = U.userIndex\n" +
                        "         left join (select commentIndex, count(likeIndex) as likeSum\n" +
                        "                    from LikeOrUnlike\n" +
                        "                    where good = 'Y'\n" +
                        "                      and status = 'Y'\n" +
                        "                    group by commentIndex) L on L.commentIndex = C.commentIndex\n" +
                        "         left join (select commentIndex, count(likeIndex) as unlikeSum\n" +
                        "                    from LikeOrUnlike\n" +
                        "                    where good = 'N'\n" +
                        "                      and status = 'Y'\n" +
                        "                    group by commentIndex) UL on UL.commentIndex = C.commentIndex\n" +
                        "         left join (select commentIndex, userIndex\n" +
                        "                    from LikeOrUnlike\n" +
                        "                    where status = 'Y'\n" +
                        "                      and good = 'Y'\n" +
                        "                      and userIndex = ?) ML on ML.commentIndex = C.commentIndex\n" +
                        "         left join (select commentIndex, userIndex\n" +
                        "                    from LikeOrUnlike\n" +
                        "                    where status = 'Y'\n" +
                        "                      and good = 'N'\n" +
                        "                      and userIndex = ?) MUL on MUL.commentIndex = C.commentIndex\n" +
                        "where likeSum > 1\n" +
                        "order by likeSum desc",
                (rs, rowNum) -> new BestComment(
                        rs.getString("nickname"),
                        rs.getString("userId"),
                        rs.getString("uploadedAt"),
                        rs.getString("content"),
                        rs.getInt("likeSum"),
                        rs.getString("isLiked"),
                        rs.getInt("unlikeSum"),
                        rs.getString("isUnliked"),
                        rs.getString("best")),
                new Object[]{contentIndex,userIndex,userIndex});
    }

    // 댓글 존재하는지 확인
    public int isExistComment(int userIndex, int contentIndex){
        return this.jdbcTemplate.queryForObject("select exists(select commentIndex from Comment where userIndex=? and contentIndex=? and status='Y')",
                int.class,
                new Object[]{userIndex, contentIndex});
    }

    // 댓글 추가
    public int postComment(int userIndex, PostCommentReq postCommentReq){
        this.jdbcTemplate.update("INSERT INTO Comment (contentIndex, userIndex, commentContent) VALUES (?,?,?)",
                new Object[]{postCommentReq.getContentIndex(), userIndex, postCommentReq.getCommentContent()});
        return this.jdbcTemplate.queryForObject("select commentIndex from Comment where userIndex=? and contentIndex=? and status='Y'",
                int.class,
                new Object[]{userIndex, postCommentReq.getContentIndex()});
    }

    // 댓글 삭제
    public int patchCommentStat(int userIndex, int contentIndex){
        int commentIndex = this.jdbcTemplate.queryForObject("select commentIndex from Comment where userIndex=? and contentIndex=? and status='Y'",
                int.class,
                new Object[]{userIndex, contentIndex});
        this.jdbcTemplate.update("UPDATE Comment SET status = 'N' where userIndex=? and contentIndex=? and status='Y'",
                new Object[]{userIndex, contentIndex});
        return commentIndex;
    }

    // 좋아요나 싫어요 눌렀는지 확인
    public int isExistsLikeOrUnlike(int userIndex, int commentIndex){
        return this.jdbcTemplate.queryForObject("select exists(select likeIndex from LikeOrUnlike where userIndex = ? and commentIndex = ? and status ='Y')",
                int.class,
                new Object[]{userIndex, commentIndex});
    }

    // 좋아요 눌렀는지 확인
    public int isExistsLike(int userIndex, int commentIndex){
        return this.jdbcTemplate.queryForObject("select exists(select likeIndex from LikeOrUnlike where userIndex = ? and commentIndex = ? and status ='Y' and good = 'Y')",
                int.class,
                new Object[]{userIndex, commentIndex});
    }

    // 좋아요 추가
    public int postCommentLike(int userIndex, int commentIndex){
        this.jdbcTemplate.update("INSERT INTO LikeOrUnlike (commentIndex, good, userIndex) VALUES (?,'Y',?)",
                new Object[]{commentIndex, userIndex});
        return this.jdbcTemplate.queryForObject("select likeIndex from LikeOrUnlike where userIndex=? and commentIndex=? and status='Y' and good = 'Y'",
                int.class,
                new Object[]{userIndex, commentIndex});
    }

    // 좋아요 삭제
    public int patchCommentLikeStat(int userIndex, int commentIndex){
        int likeIndex = this.jdbcTemplate.queryForObject("select likeIndex from LikeOrUnlike where userIndex=? and commentIndex=? and status='Y' and good = 'Y'",
                int.class,
                new Object[]{userIndex, commentIndex});
        this.jdbcTemplate.update("UPDATE LikeOrUnlike SET status='N' where userIndex=? and commentIndex=? and status='Y' and good = 'Y'",
                new Object[]{userIndex, commentIndex});
        return likeIndex;
    }

    // 싫어요 눌렀는지 확인
    public int isExistsUnlike(int userIndex, int commentIndex){
        return this.jdbcTemplate.queryForObject("select exists(select likeIndex from LikeOrUnlike where userIndex = ? and commentIndex = ? and status ='Y' and good = 'N')",
                int.class,
                new Object[]{userIndex, commentIndex});
    }

    // 싫어요 추가
    public int postCommentUnlike(int userIndex, int commentIndex){
        this.jdbcTemplate.update("INSERT INTO LikeOrUnlike (commentIndex, good, userIndex) VALUES (?,'N',?)",
                new Object[]{commentIndex, userIndex});
        return this.jdbcTemplate.queryForObject("select likeIndex from LikeOrUnlike where userIndex=? and commentIndex=? and status='Y' and good = 'N'",
                int.class,
                new Object[]{userIndex, commentIndex});
    }

    // 싫어요 삭제
    public int patchCommentUnlikeStat(int userIndex, int commentIndex){
        int unlikeIndex = this.jdbcTemplate.queryForObject("select likeIndex from LikeOrUnlike where userIndex=? and commentIndex=? and status='Y' and good = 'N'",
                int.class,
                new Object[]{userIndex, commentIndex});
        this.jdbcTemplate.update("UPDATE LikeOrUnlike SET status='N' where userIndex=? and commentIndex=? and status='Y' and good = 'N'",
                new Object[]{userIndex, commentIndex});
        return unlikeIndex;
    }

}
