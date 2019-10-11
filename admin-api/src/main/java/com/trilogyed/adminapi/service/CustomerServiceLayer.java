package com.trilogyed.adminapi.service;

import com.trilogyed.adminapi.domain.CustomerViewModel;
import com.trilogyed.adminapi.model.Customer;
import com.trilogyed.adminapi.model.LevelUp;
import com.trilogyed.adminapi.util.feign.CustomerServiceClient;
import com.trilogyed.adminapi.util.feign.LevelUpServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class CustomerServiceLayer {
    private CustomerServiceClient customerClient;
    private LevelUpServiceClient levelUpClient;

    @Autowired
    public CustomerServiceLayer(CustomerServiceClient customerClient, LevelUpServiceClient levelUpClient) {
        this.customerClient = customerClient;
        this.levelUpClient = levelUpClient;
    }

    @Transactional
    public CustomerViewModel createCustomer(Customer customer)
    {
        customer = customerClient.createCustomer(customer);
        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(customer.getCustomerId());
        levelUp.setPoints(0);
        levelUp.setMemberDate(LocalDate.now());
        levelUp = levelUpClient.createLevelUp(levelUp);
        return buildCustomerViewModel(customer);
    }

    public CustomerViewModel getCustomer(Integer customerId)
    {
        Customer customer = customerClient.getCustomerByCustomerId(customerId);
        return buildCustomerViewModel(customer);
    }

    public List<CustomerViewModel> getAllCustomers()
    {
        List<CustomerViewModel> cvmList = new ArrayList<>();
        List<Customer> customers = customerClient.getAllCustomers();
        customers.stream().forEach(customer ->
        {
            CustomerViewModel cvm = buildCustomerViewModel(customer);
            cvmList.add(cvm);
        });
        return cvmList;
    }

    public void updateCustomer(CustomerViewModel cvm)
    {
        Customer customer = customerClient.getCustomerByCustomerId(cvm.getCustomerId());
        customer.setFirstName(cvm.getFirstName());
        customer.setLastName(cvm.getLastName());
        customer.setStreet(cvm.getStreet());
        customer.setCity(cvm.getCity());
        customer.setZip(cvm.getZip());
        customer.setEmail(cvm.getEmail());
        customer.setPhone(cvm.getPhone());
        customerClient.updateCustomer(customer);
        List<LevelUp> levelUpList = levelUpClient.findLevelUpsByCustomerId(customer.getCustomerId());
        LevelUp levelUp = deleteExtraLevelUps(levelUpList);
        levelUp.setPoints(cvm.getPoints());
        levelUpClient.updateLevelUp(levelUp);
    }

    public void deleteCustomer(Integer customerId)

    {
        levelUpClient.deleteLevelUpByCustomerId(customerId);
        customerClient.deleteCustomer(customerId);
    }

    public void deleteLevelUpByLevelUpId(Integer levelUpId)
    {
        levelUpClient.deleteByLevelUpId(levelUpId);
    }

    /** Helper Method - Building View Model */

    public CustomerViewModel buildCustomerViewModel(Customer customer)
    {
        if (customer==null) return null;
        List<LevelUp> levelUpList = levelUpClient.findLevelUpsByCustomerId(customer.getCustomerId());
        LevelUp levelUp = deleteExtraLevelUps(levelUpList);

        CustomerViewModel cvm = new CustomerViewModel();
        cvm.setCustomerId(customer.getCustomerId());
        cvm.setFirstName(customer.getFirstName());
        cvm.setLastName(customer.getLastName());
        cvm.setStreet(customer.getStreet());
        cvm.setCity(customer.getCity());
        cvm.setZip(customer.getZip());
        cvm.setEmail(customer.getEmail());
        cvm.setPhone(customer.getPhone());
        cvm.setLevelUpId(levelUp.getLevelUpId());
        cvm.setPoints(levelUp.getPoints());
        cvm.setMemberDate(levelUp.getMemberDate());
        return cvm;
    }

    /** Deleting the LevelUp Id's generated in system as each customer to have only one levelUp Id */

    public LevelUp deleteExtraLevelUps(List<LevelUp> levelUpList)
    {
        Comparator<LevelUp> maxId = Comparator.comparing(LevelUp::getLevelUpId);
        Comparator<LevelUp> minMemberDate = Comparator.comparing((LevelUp::getMemberDate));

        LevelUp maxIdLevelUp = levelUpList.stream().max(maxId).get();
        LevelUp minMemberDateLevelUp = levelUpList.stream().min(minMemberDate).get();
        maxIdLevelUp.setMemberDate(minMemberDateLevelUp.getMemberDate());

        for (LevelUp level: levelUpList)
        {
            if(level.getLevelUpId()!=maxIdLevelUp.getLevelUpId())
            {
                maxIdLevelUp.setPoints(maxIdLevelUp.getPoints()+level.getPoints());
                levelUpClient.deleteByLevelUpId(level.getLevelUpId());
            }
        }
        levelUpClient.updateLevelUp(maxIdLevelUp);
        return maxIdLevelUp;
    }
}
