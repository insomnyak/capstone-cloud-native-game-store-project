package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.LevelUp;
import com.trilogyed.retailapiservice.exception.LevelUpServiceUnavailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "U2-LEVEL-UP-SERVICE", fallback = LevelUpServiceClientFallback.class)
public interface LevelUpServiceClient {

    @GetMapping("/levelUp/{levelUpId}")
    public LevelUp findByLevelUpId(@PathVariable Integer levelUpId);

    @GetMapping("/levelUp/count/{levelUpId}")
    public Integer countByLevelUpId(@PathVariable Integer levelUpId);

    @GetMapping("/levelUp/count/customer/{customerId}")
    public Integer countLevelUpsByCustomerId(@PathVariable Integer customerId);

    @GetMapping("/levelUp/customer/{customerId}")
    public List<LevelUp> findLevelUpsByCustomerId(@PathVariable Integer customerId);
}

@Component
class LevelUpServiceClientFallback implements LevelUpServiceClient {

    @Override
    public LevelUp findByLevelUpId(@PathVariable Integer levelUpId) {
        throw levelUpServiceUnavailableException();
    }

    @Override
    public Integer countByLevelUpId(@PathVariable Integer levelUpId) {
        throw levelUpServiceUnavailableException();
    }

    @Override
    public Integer countLevelUpsByCustomerId(@PathVariable Integer customerId) {
        throw levelUpServiceUnavailableException();
    }

    @Override
    public List<LevelUp> findLevelUpsByCustomerId(@PathVariable Integer customerId) {
        throw levelUpServiceUnavailableException();
    }

    private LevelUpServiceUnavailableException levelUpServiceUnavailableException() {
        return new LevelUpServiceUnavailableException("Level Up Service is not available.");
    }

}