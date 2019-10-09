package com.trilogyed.customerservice.daoTests;

import com.trilogyed.customerservice.dao.CustomerDao;
import com.trilogyed.customerservice.model.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerDaoTests {

    @Autowired
    CustomerDao dao;

    @Before
    public void setUp() throws Exception
    {
        List<Customer> customers = dao.getAllCustomers();
        customers.stream()
                .forEach(customer -> {
                    dao.deleteCustomer(customer.getCustomerId());
                });
    }

    @Test
    public void addGetUpdateGetAllDelete()
    {
        /** Add Customer Tests */
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Smith");
        customer.setStreet("Park Ave");
        customer.setCity("Manhattan");
        customer.setZip("10019");
        customer.setEmail("js@pens.com");
        customer.setPhone("123-456-789");

        customer = dao.addCustomer(customer);

        /** Get Customer Tests */
        Customer customer1 = dao.getCustomerByCustomerId(customer.getCustomerId());
        assertEquals(customer,customer1);

        /** Update Product Tests */
        customer.setStreet("Central Park Ave");
        customer.setCity("New York City");

        dao.updateCustomer(customer);
        customer1 = dao.getCustomerByCustomerId(customer.getCustomerId());

        assertEquals(customer,customer1);

        /** Get All Customers */

        List<Customer> customerList = dao.getAllCustomers();

        assertEquals(customerList.size(),1);

        /** Delete Customer */

        dao.deleteCustomer(customer.getCustomerId());

        customer1 = dao.getCustomerByCustomerId(customer.getCustomerId());

        assertNull(customer1);
    }

    @Test
    public void findConstraints()
    {
        /** Test null first name */
        try
        {
            Customer customer = new Customer();
            customer.setLastName("Smith");
            customer.setStreet("Park Ave");
            customer.setCity("Manhattan");
            customer.setZip("10019");
            customer.setEmail("js@pens.com");
            customer.setPhone("123-456-789");
            customer = dao.addCustomer(customer);
        }catch(Exception e)
        {
            System.out.println("First name mandatory.");
        }

        /** Test null last name */
        try
        {
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setStreet("Park Ave");
            customer.setCity("Manhattan");
            customer.setZip("10019");
            customer.setEmail("js@pens.com");
            customer.setPhone("123-456-789");
            customer = dao.addCustomer(customer);
        }catch(Exception e)
        {
            System.out.println("Last name mandatory.");
        }

        /** Test null street */
        try
        {
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Smith");
            customer.setCity("Manhattan");
            customer.setZip("10019");
            customer.setEmail("js@pens.com");
            customer.setPhone("123-456-789");
            customer = dao.addCustomer(customer);
        }catch(Exception e)
        {
            System.out.println("Street manadatory.");
        }

        /** Test null city */
        try
        {
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Smith");
            customer.setStreet("Park Ave");
            customer.setZip("10019");
            customer.setEmail("js@pens.com");
            customer.setPhone("123-456-789");
            customer = dao.addCustomer(customer);
        }catch(Exception e)
        {
            System.out.println("City mandatory.");
        }

        /** Test null zip */
        try
        {
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Smith");
            customer.setStreet("Park Ave");
            customer.setCity("Manhattan");
            customer.setEmail("js@pens.com");
            customer.setPhone("123-456-789");
            customer = dao.addCustomer(customer);
        }catch(Exception e)
        {
            System.out.println("Zip mandatory.");
        }

        /** Test null email */
        try
        {
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Smith");
            customer.setStreet("Park Ave");
            customer.setCity("Manhattan");
            customer.setZip("10019");
            customer.setPhone("123-456-789");
            customer = dao.addCustomer(customer);
        }catch(Exception e)
        {
            System.out.println("Email mandatory.");
        }

        /** Test null phone */
        try
        {
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Smith");
            customer.setStreet("Park Ave");
            customer.setCity("Manhattan");
            customer.setZip("10019");
            customer.setEmail("js@pens.com");
            customer = dao.addCustomer(customer);
        }catch(Exception e)
        {
            System.out.println("Phone mandatory.");
        }

        /** Test phone characters above limit */
        try
        {
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Smith");
            customer.setStreet("Park Ave");
            customer.setCity("Manhattan");
            customer.setZip("10019");
            customer.setEmail("js@pens.com");
            customer.setPhone("1234567890986523546346366662345246");
            customer = dao.addCustomer(customer);
        }catch(Exception e)
        {
            System.out.println("Phone number is limited to 20 characters.");
        }
    }

}
