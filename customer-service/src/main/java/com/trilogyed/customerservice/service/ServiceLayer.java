package com.trilogyed.customerservice.service;

import com.trilogyed.customerservice.dao.CustomerDao;
import com.trilogyed.customerservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ServiceLayer {
    private CustomerDao dao;

    @Autowired
    public ServiceLayer(CustomerDao dao) {
        this.dao = dao;
    }

    @Transactional
    public Customer createCustomer(Customer customer)
    {
        return dao.addCustomer(customer);
    }

    public Customer findCustomer(Integer customerId)
    {
        return dao.getCustomerByCustomerId(customerId);
    }

    public List<Customer> findAllCustomers()
    {
        return dao.getAllCustomers();
    }

    public void updateCustomer(Customer customer)
    {
        dao.updateCustomer(customer);
    }

    public void deleteCustomer(Integer customerId)
    {
        dao.deleteCustomer(customerId);
    }
}
