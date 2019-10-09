package com.trilogyed.levelupservice.dao;

import com.trilogyed.levelupservice.model.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class LevelUpDaoJdbcTemplateImpl implements LevelUpDao {

    private static final String INSERT_LEVEL_UP_SQL =
            "insert into level_up (customer_id, points, member_date) values (?,?,?)";
    private static final String UPDATE_LEVEL_UP_SQL =
            "update level_up set customer_id = ?, points = ?, member_date = ? where level_up_id = ?";
    private static final String DELETE_LEVEL_UP_SQL =
            "delete from level_up where level_up_id = ?";
    private static final String SELECT_LEVEL_UP_SQL =
            "select * from level_up where level_up_id = ?";
    private static final String SELECT_ALL_LEVEL_UPS_SQL =
            "select * from level_up";
    private static final String SELECT_LEVEL_UPS_BY_CUSTOMER_ID_SQL =
            "select * from level_up where customer_id = ?";
    private static final String COUNT_LEVEL_UP_SQL =
            "select count(level_up_id) from level_up where level_up_id = ?";
    private static final String COUNT_LEVEL_UPS_BY_CUSTOMER_ID_SQL =
            "select count(customer_id) from level_up where customer_id = ?";
    private static final String SELECT_EARLIEST_MEMBER_DATE_BY_CUSTOMER_ID_SQL =
            "select min(member_date) from level_up where customer_id = ?";
    private static final String DELETE_BY_CUSTOMER_ID_SQL =
            "delete from level_up where customer_id = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public LevelUpDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional
    @Override
    public LevelUp add(LevelUp levelUp) {
        jdbcTemplate.update(INSERT_LEVEL_UP_SQL,
                levelUp.getCustomerId(),
                levelUp.getPoints(),
                levelUp.getMemberDate());
        Integer id = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
        levelUp.setLevelUpId(id);
        return levelUp;
    }

    @Override
    public LevelUp find(Integer levelUpId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_LEVEL_UP_SQL, this::mapRowToLevelUp, levelUpId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(LevelUp levelUp) {
        jdbcTemplate.update(UPDATE_LEVEL_UP_SQL,
                levelUp.getCustomerId(),
                levelUp.getPoints(),
                levelUp.getMemberDate(),
                levelUp.getLevelUpId());
    }

    @Override
    public void delete(Integer levelUpId) {
        jdbcTemplate.update(DELETE_LEVEL_UP_SQL, levelUpId);
    }

    @Override
    public List<LevelUp> findAll() {
        return jdbcTemplate.query(SELECT_ALL_LEVEL_UPS_SQL, this::mapRowToLevelUp);
    }

    @Override
    public Integer count(Integer levelUpId) {
        return jdbcTemplate.queryForObject(COUNT_LEVEL_UP_SQL, Integer.class, levelUpId);
    }

    @Override
    public Integer countByCustomerId(Integer customerId) {
        return jdbcTemplate.queryForObject(COUNT_LEVEL_UPS_BY_CUSTOMER_ID_SQL, Integer.class, customerId);
    }

    @Override
    public List<LevelUp> findByCustomerId(Integer customerId) {
        return jdbcTemplate.query(SELECT_LEVEL_UPS_BY_CUSTOMER_ID_SQL, this::mapRowToLevelUp, customerId);
    }

    @Override
    public LocalDate findEarliestCustomerMemberDate(Integer customerId) {
        return jdbcTemplate.queryForObject(SELECT_EARLIEST_MEMBER_DATE_BY_CUSTOMER_ID_SQL, LocalDate.class, customerId);
    }

    @Override
    public void deleteByCustomerId(Integer customerId) {
        jdbcTemplate.update(DELETE_BY_CUSTOMER_ID_SQL, customerId);
    }

    private LevelUp mapRowToLevelUp(ResultSet rs, int rowNum) throws SQLException {
        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(rs.getInt("level_up_id"));
        levelUp.setCustomerId(rs.getInt("customer_id"));
        levelUp.setMemberDate(LocalDate.parse(rs.getString("member_date")));
        levelUp.setPoints(rs.getInt("points"));
        return levelUp;
    }
}
