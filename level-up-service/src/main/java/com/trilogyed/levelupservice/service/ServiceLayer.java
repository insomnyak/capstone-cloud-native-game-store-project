package com.trilogyed.levelupservice.service;

import com.insomnyak.util.MapClasses;
import com.trilogyed.levelupservice.dao.LevelUpDao;
import com.trilogyed.levelupservice.model.LevelUp;
import com.trilogyed.queue.LevelUpViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        return dao.add(levelUp);
    }

    @Transactional
    public LevelUpViewModel save(LevelUpViewModel luvm) {
        LevelUp levelUp = build(luvm);
        levelUp = dao.add(levelUp);

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

    private LevelUp build(LevelUpViewModel levelUpViewModel) {
        return (new MapClasses<>(levelUpViewModel, LevelUp.class)).mapFirstToSecond(false);
    }

    private LevelUpViewModel build(LevelUp levelUp) {
        return (new MapClasses<>(levelUp, LevelUpViewModel.class)).mapFirstToSecond(false);
    }
}
