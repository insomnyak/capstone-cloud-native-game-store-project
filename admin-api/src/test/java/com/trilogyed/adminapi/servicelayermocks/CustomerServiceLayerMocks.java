package com.trilogyed.adminapi.servicelayermocks;

import com.trilogyed.adminapi.domain.CustomerViewModel;
import com.trilogyed.adminapi.model.Customer;
import com.trilogyed.adminapi.model.LevelUp;
import com.trilogyed.adminapi.service.CustomerServiceLayer;
import com.trilogyed.adminapi.util.feign.CustomerServiceClient;
import com.trilogyed.adminapi.util.feign.LevelUpServiceClient;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class CustomerServiceLayerMocks {
    private CustomerServiceClient customerClient;
    private LevelUpServiceClient levelUpClient;
    private CustomerServiceLayer sl;

    @Test
    public void addGetUpdateDeleteCustomer()
    {
        customerClient=mock(CustomerServiceClient.class);
        levelUpClient=mock(LevelUpServiceClient.class);

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

        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(10);
        levelUp.setPoints(0);
        levelUp.setMemberDate(LocalDate.now());

        LevelUp savedLevelUp = new LevelUp();
        savedLevelUp.setLevelUpId(1010);
        savedLevelUp.setCustomerId(10);
        savedLevelUp.setPoints(0);
        savedLevelUp.setMemberDate(LocalDate.now());

        LevelUp anotherLevelUp = new LevelUp();
        anotherLevelUp.setLevelUpId(1020);
        anotherLevelUp.setCustomerId(20);
        anotherLevelUp.setPoints(0);
        anotherLevelUp.setMemberDate(LocalDate.now());

        List<LevelUp> levelUpList = new ArrayList<>();
        levelUpList.add(savedLevelUp);
        levelUpList.add(anotherLevelUp);

        List<LevelUp> levelUpListByCustomerId1 = new ArrayList<>();
        levelUpListByCustomerId1.add(savedLevelUp);

        List<LevelUp> levelUpListByCustomerId2 = new ArrayList<>();
        levelUpListByCustomerId2.add(anotherLevelUp);

        LevelUp updateLevelUp = new LevelUp();
        updateLevelUp.setLevelUpId(1030);
        updateLevelUp.setCustomerId(30);
        updateLevelUp.setPoints(100);
        updateLevelUp.setMemberDate(LocalDate.now());

        List<LevelUp> levelUpListByUpdatedCustomerId = new ArrayList<>();
        levelUpListByUpdatedCustomerId.add(updateLevelUp);

        LevelUp deleteLevelUp = new LevelUp();
        deleteLevelUp.setLevelUpId(1040);
        deleteLevelUp.setCustomerId(40);
        deleteLevelUp.setPoints(40);
        deleteLevelUp.setMemberDate(LocalDate.now());

        doReturn(savedCustomer).when(customerClient).createCustomer(customer);
        doReturn(savedCustomer).when(customerClient).getCustomerByCustomerId(10);
        doReturn(customerList).when(customerClient).getAllCustomers();
        doReturn(anotherCustomer).when(customerClient).getCustomerByCustomerId(20);
        doNothing().when(customerClient).updateCustomer(updateCustomer);
        doReturn(updateCustomer).when(customerClient).getCustomerByCustomerId(30);
        doNothing().when(customerClient).deleteCustomer(40);
        doReturn(null).when(customerClient).getCustomerByCustomerId(40);

        doReturn(savedLevelUp).when(levelUpClient).createLevelUp(levelUp);
        doReturn(savedLevelUp).when(levelUpClient).findByLevelUpId(1010);
        doReturn(levelUpList).when(levelUpClient).findAllLevelUps();
        doReturn(levelUpListByCustomerId1).when(levelUpClient).findLevelUpsByCustomerId(10);
        doReturn(levelUpListByCustomerId2).when(levelUpClient).findLevelUpsByCustomerId(20);
        doNothing().when(levelUpClient).updateLevelUp(updateLevelUp);
        doReturn(levelUpListByUpdatedCustomerId).when(levelUpClient).findLevelUpsByCustomerId(30);
        doNothing().when(levelUpClient).deleteLevelUpByCustomerId(40);
        doReturn(null).when(levelUpClient).findLevelUpsByCustomerId(40);
        doNothing().when(levelUpClient).deleteByLevelUpId(40);
        doReturn(null).when(levelUpClient).findByLevelUpId(1040);

        sl = new CustomerServiceLayer(customerClient,levelUpClient);

        /** Tests for creating a new Customer with LevelUp */
        CustomerViewModel addingCustomer = sl.createCustomer(customer);
        CustomerViewModel fetchCustomerAdded = sl.getCustomer(addingCustomer.getCustomerId());
        assertEquals(addingCustomer,fetchCustomerAdded);

        /** Tests to get all customers lists */
        List<CustomerViewModel> cvmList = sl.getAllCustomers();
        assertEquals(cvmList.size(),2);

        /** Tests to update Customer/LevelUp */
        CustomerViewModel getCustomerVM = sl.getCustomer(30);
        sl.updateCustomer(getCustomerVM);
        CustomerViewModel fetchUpdatedCustomer = sl.getCustomer(getCustomerVM.getCustomerId());
        assertEquals(getCustomerVM,fetchUpdatedCustomer);

        /** Tests to delete Customer */
        sl.deleteCustomer(40);
        CustomerViewModel cvmDeleted = sl.getCustomer(40);
        assertNull(cvmDeleted);

        /** Tests to delete LevelUp */
        sl.deleteLevelUpByLevelUpId(1040);
        assertNull(levelUpClient.findByLevelUpId(1040));

    }
}
