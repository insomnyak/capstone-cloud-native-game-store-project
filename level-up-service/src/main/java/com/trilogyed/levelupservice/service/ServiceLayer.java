package com.trilogyed.levelupservice.service;

import com.trilogyed.levelupservice.dao.LevelUpDao;
import com.trilogyed.levelupservice.model.LevelUp;
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

    public LevelUp find(Integer levelUpId) {
        return dao.find(levelUpId);
    }

    public void update(LevelUp levelUp) {
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
}
