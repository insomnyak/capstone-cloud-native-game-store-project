package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.Customer;
import com.trilogyed.retailapiservice.exception.CustomerServiceUnavailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "U2-CUSTOMER-SERVICE", configuration = DefaultFeignClientConfiguration.class)
public interface CustomerServiceClient {
    @GetMapping(value = "/customer/{customerId}")
    public Customer getCustomerByCustomerId(@PathVariable(name = "customerId") int customerId);
}