package com.trilogyed.levelupservice.service;

import com.insomnyak.util.MapClasses;
import com.trilogyed.levelupservice.dao.LevelUpDao;
import com.trilogyed.levelupservice.model.LevelUp;
import com.trilogyed.queue.shared.viewmodel.LevelUpViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceLayer {

    LevelUpDao dao;

    @Autowired
    public ServiceLayer(LevelUpDao dao) {
        this.dao = dao;
    }

    @Transactional
    public LevelUp save(LevelUp levelUp) {
        // check if customerId exists
        // add levelUp points to existing entry
        // if more than one customerId entry exists combined entries
        Integer count = dao.countByCustomerId(levelUp.getCustomerId());
        if (count > 1) {
            return cleanLevelUp(levelUp.getCustomerId(), levelUp.getPoints());
        } else if (count == 1) {
            LevelUp existingLevelUp = dao.findByCustomerId(levelUp.getCustomerId()).get(0);
            existingLevelUp.setPoints(existingLevelUp.getPoints() + levelUp.getPoints());
            dao.update(existingLevelUp);
            return existingLevelUp;
        } else {
            return dao.add(levelUp);
        }
    }

    @Transactional
    public LevelUpViewModel save(LevelUpViewModel luvm) {
        if (luvm.getMemberDate() == null) {
            luvm.setMemberDate(LocalDate.now());
        }
        LevelUp levelUp = build(luvm);
        levelUp = save(levelUp);

        luvm.setLevelUpId(levelUp.getLevelUpId());
        return luvm;
    }

    public LevelUp find(Integer levelUpId) {
        return dao.find(levelUpId);
    }

    public void update(LevelUp levelUp) {
        dao.update(levelUp);
    }

    public void update(LevelUpViewModel luvm) {
        LevelUp levelUp = build(luvm);
        dao.update(levelUp);
    }

    public void delete(Integer levelUpId) {
        dao.delete(levelUpId);
    }

    public List<LevelUp> findAll() {
        return dao.findAll();
    }

    public Integer count(Integer levelUpId) {
        return dao.count(levelUpId);
    }

    public Integer countByCustomerId(Integer customerId) {
        return dao.countByCustomerId(customerId);
    }

    public List<LevelUp> findByCustomerId(Integer customerId) {
        return dao.findByCustomerId(customerId);
    }

    public LocalDate findEarliestCustomerMemberDate(Integer customerId) {
        return dao.findEarliestCustomerMemberDate(customerId);
    }

    public void deleteByCustomerId(Integer customerId) {
        dao.deleteByCustomerId(customerId);
    }

    // ******************
    // HELPER METHODS
    // ******************

    @Transactional
    public LevelUp cleanLevelUp(Integer customerId, Integer newPoints) {
        if (newPoints == null) newPoints = 0;

        List<LevelUp> levelUps = dao.findByCustomerId(customerId);
        if (levelUps.size() == 0) return null;
        if (levelUps.size() == 1 && newPoints > 0) {
            LevelUp levelUp = levelUps.get(0);
            levelUp.setPoints(levelUp.getPoints() + newPoints);
            dao.update(levelUp);
        }

        LocalDate memberDate = dao.findEarliestCustomerMemberDate(customerId);

        List<Integer> levelUpIdsToRemove = new ArrayList<>();
        Integer totalPoints = 0;
        Integer maxLevelUpId = null;
        LevelUp newLevelUp = null;
        for (LevelUp levelUp : levelUps) {
            totalPoints += levelUp.getPoints();
            if (maxLevelUpId == null) {
                maxLevelUpId = levelUp.getLevelUpId();
                newLevelUp = copy(levelUp);
            } else {
                levelUpIdsToRemove.add(levelUp.getLevelUpId());
            }
        }

        // consolidate multiple LevelUps for customerId
        newLevelUp.setPoints(totalPoints + newPoints);
        newLevelUp.setMemberDate(memberDate);
        dao.update(newLevelUp);

        // delete duplicates
        levelUpIdsToRemove.forEach(id -> dao.delete(id));

        return newLevelUp;
    }

    private LevelUp build(LevelUpViewModel levelUpViewModel) {
        return (new MapClasses<>(levelUpViewModel, LevelUp.class)).mapFirstToSecond(false);
    }

    private LevelUpViewModel build(LevelUp levelUp) {
        return (new MapClasses<>(levelUp, LevelUpViewModel.class)).mapFirstToSecond(false);
    }

    private LevelUp copy(LevelUp levelUp) {
        return (new MapClasses<>(levelUp, LevelUp.class)).mapFirstToSecond(false);
    }
}
