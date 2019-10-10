package com.trilogyed.adminapi.util.feign;

import com.trilogyed.adminapi.model.LevelUp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "U2-level-up-service")
@RequestMapping("/levelUp")
public interface LevelUpServiceClient {
    @PostMapping
    public LevelUp createLevelUp(@RequestBody @Valid LevelUp levelUp);

    @GetMapping("/{levelUpId}")
    public LevelUp findByLevelUpId(@PathVariable Integer levelUpId);

    @PutMapping
    public void updateLevelUp(@RequestBody @Valid LevelUp levelUp);

    @DeleteMapping("/{levelUpId}")
    public void deleteByLevelUpId(@PathVariable Integer levelUpId);

    @GetMapping
    public List<LevelUp> findAllLevelUps();

    @GetMapping("/count/{levelUpId}")
    public Integer countByLevelUpId(@PathVariable Integer levelUpId);

    @GetMapping("/count/customer/{customerId}")
    public Integer countLevelUpsByCustomerId(@PathVariable Integer customerId);

    @GetMapping("/customer/{customerId}")
    public List<LevelUp> findLevelUpsByCustomerId(@PathVariable Integer customerId);

    @GetMapping("/customer/{customerId}/memberDate")
    public LocalDate findEarliestMemberDateForCustomerId(@PathVariable Integer customerId);

    @DeleteMapping("/customer/{customerId}")
    public void deleteLevelUpByCustomerId(@PathVariable Integer customerId);
}