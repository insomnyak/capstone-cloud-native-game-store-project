package com.trilogyed.customerservice.dao;

import com.trilogyed.customerservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerDaoImpl implements CustomerDao{

    private static final String INSERT_INTO=
            "insert into customer (first_name,last_name,street,city,zip,email,phone) values (?,?,?,?,?,?,?)";
    private static final String GET_CUSTOMER=
            "select * from customer where customer_id=?";
    private static final String GET_ALL_CUSTOMERS=
            "select * from customer";
    private static final String UPDATE_CUSTOMER=
            "update customer set first_name=?,last_name=?,street=?,city=?,zip=?,email=?,phone=? where customer_id=?";
    private static final String DELETE_CUSTOMER=
            "delete from customer where customer_id=?";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** Mapping result data set to the DB */
    private Customer mapRowToCustomer(ResultSet rs, int rowNum) throws SQLException
    {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setStreet(rs.getString("street"));
        customer.setCity(rs.getString("city"));
        customer.setZip(rs.getString("zip"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        return customer;
    }

    @Override
    @Transactional
    public Customer addCustomer(Customer customer) {
        jdbcTemplate.update(INSERT_INTO,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getStreet(),
                customer.getCity(),
                customer.getZip(),
                customer.getEmail(),
                customer.getPhone());
        int id = jdbcTemplate.queryForObject("select last_insert_id()",Integer.class);
        customer.setCustomerId(id);
        return customer;
    }

    @Override
    public Customer getCustomerByCustomerId(Integer customerId) {
        try{
            return jdbcTemplate.queryForObject(GET_CUSTOMER,this::mapRowToCustomer,customerId);
        }catch(EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return jdbcTemplate.query(GET_ALL_CUSTOMERS,this::mapRowToCustomer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        jdbcTemplate.update(UPDATE_CUSTOMER,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getStreet(),
                customer.getCity(),
                customer.getZip(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCustomerId());
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        jdbcTemplate.update(DELETE_CUSTOMER,customerId);
    }
}
