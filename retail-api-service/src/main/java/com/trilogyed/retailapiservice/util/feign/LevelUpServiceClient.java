package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.LevelUp;
import com.trilogyed.retailapiservice.exception.LevelUpServiceUnavailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "U2-LEVEL-UP-SERVICE", fallback = LevelUpServiceClientFallback.class)
@RequestMapping("/levelUp")
public interface LevelUpServiceClient {

    @GetMapping("/{levelUpId}")
    public LevelUp findByLevelUpId(@PathVariable Integer levelUpId);

    @GetMapping("/count/{levelUpId}")
    public Integer countByLevelUpId(@PathVariable Integer levelUpId);

    @GetMapping("/count/customer/{customerId}")
    public Integer countLevelUpsByCustomerId(@PathVariable Integer customerId);

    @GetMapping("/customer/{customerId}")
    public List<LevelUp> findLevelUpsByCustomerId(@PathVariable Integer customerId);

//    @PutMapping("/customer/{customerId}/consolidate")
//    public LevelUp consolidateLevelUpsByCustomerId(@PathVariable Integer customerId);

//    @PostMapping
//    public LevelUp createLevelUp(@RequestBody @Valid LevelUp levelUp);

//    @PutMapping
//    public void updateLevelUp(@RequestBody @Valid LevelUp levelUp);

//    @DeleteMapping("/{levelUpId}")
//    public void deleteByLevelUpId(@PathVariable Integer levelUpId);
//
//    @GetMapping
//    public List<LevelUp> findAllLevelUps();

//    @GetMapping("/customer/{customerId}/memberDate")
//    public LocalDate findEarliestMemberDateForCustomerId(@PathVariable Integer customerId);
//
//    @DeleteMapping("/customer/{customerId}")
//    public void deleteLevelUpByCustomerId(@PathVariable Integer customerId);
}

@Component
class LevelUpServiceClientFallback implements LevelUpServiceClient {

    @Override
    public LevelUp findByLevelUpId(Integer levelUpId) {
        throw levelUpServiceUnavailableException();
    }

    @Override
    public Integer countByLevelUpId(Integer levelUpId) {
        throw levelUpServiceUnavailableException();
    }

    @Override
    public Integer countLevelUpsByCustomerId(Integer customerId) {
        throw levelUpServiceUnavailableException();
    }

    @Override
    public List<LevelUp> findLevelUpsByCustomerId(Integer customerId) {
        throw levelUpServiceUnavailableException();
    }

    private LevelUpServiceUnavailableException levelUpServiceUnavailableException() {
        return new LevelUpServiceUnavailableException("Level Up Service is not available.");
    }

}