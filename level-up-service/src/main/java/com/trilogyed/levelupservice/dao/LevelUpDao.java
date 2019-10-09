package com.trilogyed.levelupservice.dao;

import com.insomnyak.interfaces.EntityDao;
import com.trilogyed.levelupservice.model.LevelUp;

import java.time.LocalDate;
import java.util.List;

public interface LevelUpDao {
    LevelUp add(LevelUp levelUp);
    LevelUp find(Integer levelUpId);
    void update(LevelUp levelUp);
    void delete(Integer levelUpId);

    List<LevelUp> findAll();

    Integer count(Integer levelUpId);
    Integer countByCustomerId(Integer customerId);
    List<LevelUp> findByCustomerId(Integer customerId);
    LocalDate findEarliestCustomerMemberDate(Integer customerId);
    void deleteByCustomerId(Integer customerId);

}
