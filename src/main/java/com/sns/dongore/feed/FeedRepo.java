package com.sns.dongore.feed;

import com.sns.dongore.feed.model.Feed;
import com.sns.dongore.feed.model.PostFeedReq;
import com.sns.dongore.sensedata.SensedataRepo;
import com.sns.dongore.sensedata.model.Sensedata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository @Slf4j
public class FeedRepo {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // TODO : Make SQLS for Feed
    public Long createNewFeed(PostFeedReq req) {
        String getQuery = "INSERT INTO Feed (writer, text, title, sensedata, location)"
                + "VALUES(?, ?, ?, ?, ?)";
        // TODO : sensedata, location 참조 방식 고안
        // Object[] params = new Object[]{req.getWriterId(), req.getText(), req.getTitle(), sensedata자리, location자리};
        //jdbcTemplate.update(getQuery, params);
        String lastInsertIdQuery = "SELECT MAX(id) FROM Feed";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, Long.class);
    }

    public Boolean isFeedIdExsit(Long feedId) {
        String getQuery = "SELECT COUNT(*) FROM Feed WHERE id = ?";
        Object[] params = new Object[]{ feedId };
        return (jdbcTemplate.queryForObject(getQuery, Integer.class, params)) != 0;
    }


    public Feed findByFeedId(Long feedId){
        String getQuery = "SELECT * FROM Feed WHERE id = ?";
        Object[] params = new Object[]{ feedId };

        return jdbcTemplate.queryForObject(getQuery,
                (rs, rowNum) -> new Feed(
                        rs.getLong("id"),
                        rs.getLong("writer"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at"),
                        rs.getShort("status"),
                        rs.getString("text"),
                        rs.getString("title"),
                        rs.getLong("sensedata"),
                        rs.getLong("location")), params);

    }
}
