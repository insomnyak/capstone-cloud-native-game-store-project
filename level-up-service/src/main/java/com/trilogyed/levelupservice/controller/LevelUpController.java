package com.trilogyed.levelupservice.controller;

import com.trilogyed.levelupservice.model.LevelUp;
import com.trilogyed.levelupservice.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/levelUp")
public class LevelUpController {

    @Autowired
    ServiceLayer sl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LevelUp createLevelUp(@RequestBody @Valid LevelUp levelUp) {
        return sl.save(levelUp);
    }

    @GetMapping("/{levelUpId}")
    @ResponseStatus(HttpStatus.OK)
    public LevelUp findByLevelUpId(@PathVariable Integer levelUpId) {
        return sl.find(levelUpId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateLevelUp(@RequestBody @Valid LevelUp levelUp) {
        sl.update(levelUp);
    }

    @DeleteMapping("/{levelUpId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteByLevelUpId(@PathVariable Integer levelUpId) {
        sl.delete(levelUpId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LevelUp> findAllLevelUps() {
        return sl.findAll();
    }

    @GetMapping("/count/{levelUpId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer countByLevelUpId(@PathVariable Integer levelUpId) {
        return sl.count(levelUpId);
    }

    @GetMapping("/count/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer countLevelUpsByCustomerId(@PathVariable Integer customerId) {
        return sl.countByCustomerId(customerId);
    }

    @GetMapping("/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<LevelUp> findLevelUpsByCustomerId(@PathVariable Integer customerId) {
        return sl.findByCustomerId(customerId);
    }

    @GetMapping("/customer/{customerId}/memberDate")
    @ResponseStatus(HttpStatus.OK)
    public LocalDate findEarliestMemberDateForCustomerId(@PathVariable Integer customerId) {
        return sl.findEarliestCustomerMemberDate(customerId);
    }

    @DeleteMapping("/customer/{customerId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteLevelUpByCustomerId(@PathVariable Integer customerId) {
        sl.deleteByCustomerId(customerId);
    }
}
