package com.trilogyed.customerservice.controller;

import com.trilogyed.customerservice.dao.CustomerDao;
import com.trilogyed.customerservice.exception.NotFoundException;
import com.trilogyed.customerservice.model.Customer;
import com.trilogyed.customerservice.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
public class CustomerServiceController {

    @Autowired
    ServiceLayer sl;

    @PostMapping(value = "/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@RequestBody @Valid Customer customer)
    {
        return sl.createCustomer(customer);
    }

    @GetMapping(value = "/customer")
    @ResponseStatus(HttpStatus.OK)
    public List<Customer> getAllCustomers()
    {
        List<Customer> customerList = sl.findAllCustomers();
        return customerList;
    }

    @GetMapping(value = "/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public Customer getCustomerByCustomerId(@PathVariable(name = "customerId") int customerId)
    {
        Customer customer = sl.findCustomer(customerId);
        if(customer==null) throw new NotFoundException("No Customer exists with this id.");
        return customer;
    }

    @PutMapping(value = "/customer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCustomer(@RequestBody @Valid Customer customer, @PathVariable(name = "customerId") int customerId)
    {
        if(customer.getCustomerId()==0) customer.setCustomerId(customerId);
        if(customer.getCustomerId()!=customerId) throw new NotFoundException("No customer exist with this id.");
        sl.updateCustomer(customer);
    }

    @DeleteMapping(value = "/customer/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable(name = "customerId") int customerId)
    {
        if(sl.findCustomer(customerId)==null) throw new NotFoundException("Cannot find a customer with this id in database");
        sl.deleteCustomer(customerId);
    }
}
