package com.trilogyed.customerservice.dao;

import com.trilogyed.customerservice.model.Customer;

import java.util.List;

public interface CustomerDao {
    Customer addCustomer(Customer customer);
    Customer getCustomerByCustomerId(Integer customerId);
    List<Customer> getAllCustomers();
    void updateCustomer(Customer customer);
    void deleteCustomer(Integer customerId);
}
