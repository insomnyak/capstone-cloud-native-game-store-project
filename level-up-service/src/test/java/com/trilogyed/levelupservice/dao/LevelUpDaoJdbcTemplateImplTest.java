package com.trilogyed.levelupservice.dao;

import com.trilogyed.levelupservice.model.LevelUp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LevelUpDaoJdbcTemplateImplTest {

    @Autowired
    private LevelUpDao dao;

    @Before
    public void setUp() throws Exception {
        List<LevelUp> levelUps = dao.findAll();
        levelUps.forEach(levelUp -> dao.delete(levelUp.getLevelUpId()));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addGetUpdate_findAll_findByCustomerId_delete() {
        // add
        LevelUp levelUp = new LevelUp();
        levelUp.setPoints(55);
        levelUp.setMemberDate(LocalDate.now());
        levelUp.setCustomerId(1);
        dao.add(levelUp);
        LevelUp levelUp1 = dao.find(levelUp.getLevelUpId());
        assertEquals(levelUp, levelUp1);

        // update
        levelUp.setPoints(125);
        dao.update(levelUp);
        levelUp1 = dao.find(levelUp.getLevelUpId());
        assertEquals(levelUp, levelUp1);

        // findAll
        List<LevelUp> levelUps = dao.findAll();
        assertEquals(1, levelUps.size());

        // findByCustomerId
        levelUps = dao.findByCustomerId(1);
        assertEquals(1, levelUps.size());

        dao.delete(levelUp.getLevelUpId());
        levelUp1 = dao.find(levelUp.getLevelUpId());
        assertNull(levelUp1);
    }

    @Test
    public void count_countByCustomerId_findEarliestCustomerMemberDate_deleteByCustomerId() {
        LevelUp levelUp = new LevelUp();
        levelUp.setPoints(55);
        levelUp.setMemberDate(LocalDate.now());
        levelUp.setCustomerId(1);
        dao.add(levelUp);

        levelUp = new LevelUp();
        levelUp.setPoints(55);
        levelUp.setMemberDate(LocalDate.now().plusDays(-20L));
        levelUp.setCustomerId(1);
        dao.add(levelUp);

        // count
        Integer count = dao.count(levelUp.getLevelUpId());
        assertEquals(java.util.Optional.of(1).orElse(0), count);

        // countByCustomerId
        count = dao.countByCustomerId(levelUp.getCustomerId());
        assertEquals(java.util.Optional.of(2).orElse(0), count);

        // findEarliestCustomerMemberDate
        LocalDate memberDate = dao.findEarliestCustomerMemberDate(levelUp.getCustomerId());
        assertEquals(LocalDate.now().plusDays(-20L), memberDate);

        // deleteByCustomerId
        dao.deleteByCustomerId(levelUp.getCustomerId());
        count = dao.countByCustomerId(levelUp.getCustomerId());
        assertEquals(java.util.Optional.of(0).orElse(0), count);
    }

    @Test
    public void constraints() {
        try {
            LevelUp levelUp = new LevelUp();
            levelUp.setMemberDate(LocalDate.now());
            levelUp.setCustomerId(1);
            dao.add(levelUp);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            LevelUp levelUp = new LevelUp();
            levelUp.setPoints(55);
            levelUp.setCustomerId(1);
            dao.add(levelUp);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            LevelUp levelUp = new LevelUp();
            levelUp.setPoints(55);
            levelUp.setMemberDate(LocalDate.now());
            dao.add(levelUp);
        } catch (DataIntegrityViolationException ignore) {}

        List<LevelUp> levelUps = dao.findAll();
        assertEquals(0, levelUps.size());

    }
}