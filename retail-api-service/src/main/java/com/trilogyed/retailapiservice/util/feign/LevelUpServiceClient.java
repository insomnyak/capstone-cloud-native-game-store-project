package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.LevelUp;
import com.trilogyed.retailapiservice.exception.LevelUpServiceUnavailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "U2-LEVEL-UP-SERVICE", fallback = LevelUpServiceClientFallback.class)
public interface LevelUpServiceClient {

    @GetMapping("/levelUp/customer/{customerId}")
    public List<LevelUp> findLevelUpsByCustomerId(@PathVariable Integer customerId);
}

@Component
class LevelUpServiceClientFallback implements LevelUpServiceClient {
    @Override
    public List<LevelUp> findLevelUpsByCustomerId(@PathVariable Integer customerId) {
        throw levelUpServiceUnavailableException();
    }

    private LevelUpServiceUnavailableException levelUpServiceUnavailableException() {
        return new LevelUpServiceUnavailableException("Level Up Service is not available.");
    }

}