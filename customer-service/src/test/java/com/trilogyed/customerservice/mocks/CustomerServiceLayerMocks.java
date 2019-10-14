package com.trilogyed.customerservice.mocks;

import com.trilogyed.customerservice.dao.CustomerDao;
import com.trilogyed.customerservice.model.Customer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

public class CustomerServiceLayerMocks {
    private CustomerDao dao;

    @Test
    public void addGetUpdateDeleteCustomer()
    {
        dao=mock(CustomerDao.class);

        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Smith");
        customer.setStreet("Park Ave");
        customer.setCity("Manhattan");
        customer.setZip("10019");
        customer.setEmail("js@pens.com");
        customer.setPhone("123-456-789");

        Customer savedCustomer = new Customer();
        savedCustomer.setCustomerId(10);
        savedCustomer.setFirstName("John");
        savedCustomer.setLastName("Smith");
        savedCustomer.setStreet("Park Ave");
        savedCustomer.setCity("Manhattan");
        savedCustomer.setZip("10019");
        savedCustomer.setEmail("js@pens.com");
        savedCustomer.setPhone("123-456-789");

        Customer anotherCustomer = new Customer();
        anotherCustomer.setCustomerId(20);
        anotherCustomer.setFirstName("John");
        anotherCustomer.setLastName("Smith");
        anotherCustomer.setStreet("Park Ave");
        anotherCustomer.setCity("Manhattan");
        anotherCustomer.setZip("10019");
        anotherCustomer.setEmail("js@pens.com");
        anotherCustomer.setPhone("123-456-789");

        List<Customer> customerList = new ArrayList<>();
        customerList.add(savedCustomer);
        customerList.add(anotherCustomer);

        Customer updateCustomer = new Customer();
        updateCustomer.setCustomerId(30);
        updateCustomer.setFirstName("Joanne");
        updateCustomer.setLastName("Smith");
        updateCustomer.setStreet("Central Park Ave");
        updateCustomer.setCity("Manhattan");
        updateCustomer.setZip("10014");
        updateCustomer.setEmail("js@pens.com");
        updateCustomer.setPhone("123-456-789");

        Customer deleteCustomer = new Customer();
        deleteCustomer.setCustomerId(40);
        deleteCustomer.setFirstName("John");
        deleteCustomer.setLastName("Smith");
        deleteCustomer.setStreet("Park Ave");
        deleteCustomer.setCity("Manhattan");
        deleteCustomer.setZip("10019");
        deleteCustomer.setEmail("js@pens.com");
        deleteCustomer.setPhone("123-456-789");

        doReturn(savedCustomer).when(dao).addCustomer(customer);
        doReturn(savedCustomer).when(dao).getCustomerByCustomerId(10);
        doReturn(customerList).when(dao).getAllCustomers();
        doReturn(anotherCustomer).when(dao).getCustomerByCustomerId(20);
        doNothing().when(dao).updateCustomer(updateCustomer);
        doReturn(updateCustomer).when(dao).getCustomerByCustomerId(30);
        doNothing().when(dao).deleteCustomer(40);
        doReturn(null).when(dao).getCustomerByCustomerId(40);

        /** Tests for creating a new Customer with LevelUp */
        Customer addingCustomer = dao.addCustomer(customer);
        Customer fetchCustomerAdded = dao.getCustomerByCustomerId(addingCustomer.getCustomerId());
        assertEquals(addingCustomer,fetchCustomerAdded);

        /** Tests to get all customers lists */
        List<Customer> cvmList = dao.getAllCustomers();
        assertEquals(cvmList.size(),2);

        /** Tests to update Customer/LevelUp */
        Customer getCustomerVM = dao.getCustomerByCustomerId(30);
        dao.updateCustomer(getCustomerVM);
        Customer fetchUpdatedCustomer = dao.getCustomerByCustomerId(getCustomerVM.getCustomerId());
        assertEquals(getCustomerVM,fetchUpdatedCustomer);

        /** Tests to delete Customer */
        dao.deleteCustomer(40);
        Customer cvmDeleted = dao.getCustomerByCustomerId(40);
        assertNull(cvmDeleted);
    }
}
