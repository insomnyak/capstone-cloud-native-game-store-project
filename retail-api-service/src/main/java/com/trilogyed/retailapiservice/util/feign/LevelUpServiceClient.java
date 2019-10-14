package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.LevelUp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "U2-LEVEL-UP-SERVICE", configuration = HystrixFeignClientConfiguration.class,
        fallback = LevelUpServiceClientFallback.class)
public interface LevelUpServiceClient {

    @GetMapping("/levelUp/customer/{customerId}")
    public List<LevelUp> findLevelUpsByCustomerId(@PathVariable Integer customerId);
}

@Component
class LevelUpServiceClientFallback implements LevelUpServiceClient {
    public List<LevelUp> findLevelUpsByCustomerId(@PathVariable Integer customerId) {
        return new ArrayList<LevelUp>() {{
            add(new LevelUp() {{
                setCustomerId(-1);
                setPoints(-1);
                setLevelUpId(-1);
                setMemberDate(LocalDate.parse("1970-01-01"));
            }});
        }};
    }
}