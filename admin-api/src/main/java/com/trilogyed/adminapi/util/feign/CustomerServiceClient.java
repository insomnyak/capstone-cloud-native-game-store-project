package com.trilogyed.adminapi.util.feign;

import com.trilogyed.adminapi.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "U2-customer-service")
@RequestMapping("/customer")
public interface CustomerServiceClient {

    @PostMapping
    public Customer createCustomer(@RequestBody @Valid Customer customer);

    @GetMapping
    public List<Customer> getAllCustomers();

    @GetMapping(value = "/{customerId}")
    public Customer getCustomerByCustomerId(@PathVariable(name = "customerId") Integer customerId);

    @PutMapping
    public void updateCustomer(@RequestBody @Valid Customer customer);

    @DeleteMapping(value = "/{customerId}")
    public void deleteCustomer(@PathVariable(name = "customerId") Integer customerId);
}
