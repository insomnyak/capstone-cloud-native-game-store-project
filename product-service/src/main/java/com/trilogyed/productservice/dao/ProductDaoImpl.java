package com.trilogyed.productservice.dao;

import com.trilogyed.productservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao{

    private static final String INSERT_INTO=
            "insert into product (product_name,product_description,list_price,unit_cost) values (?,?,?,?)";
    private static final String SELECT_PRODUCT_BY_ID=
            "select * from product where product_id=?";
    private static final String SELECT_ALL_PRODUCTS=
            "select * from product";
    private static final String UPDATE_PRODUCT=
            "update product set product_name=?,product_description=?,list_price=?,unit_cost=? where product_id=?";
    private static final String DELETE_PRODUCT=
            "delete from product where product_id=?";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** Result Mappings to columns in DB */
    private Product mapRowtoProduct(ResultSet rs, int rowNum) throws SQLException
    {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));
        product.setProductDescription(rs.getString("product_description"));
        product.setListPrice(rs.getBigDecimal("list_price"));
        product.setUnitCost(rs.getBigDecimal("unit_cost"));
        return product;
    }

    @Override
    @Transactional
    public Product addProduct(Product product) {
        jdbcTemplate.update(INSERT_INTO,
                product.getProductName(),
                product.getProductDescription(),
                product.getListPrice(),
                product.getUnitCost());
        int id = jdbcTemplate.queryForObject("select last_insert_id()",Integer.class);
        product.setProductId(id);
        return product;
    }

    @Override
    public Product getProductByProductId(Integer productId) {
        try
        {
            return jdbcTemplate.queryForObject(SELECT_PRODUCT_BY_ID,this::mapRowtoProduct,productId);
        }catch(EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return jdbcTemplate.query(SELECT_ALL_PRODUCTS,this::mapRowtoProduct);
    }

    @Override
    public void updateProduct(Product product) {
        jdbcTemplate.update(UPDATE_PRODUCT,
                product.getProductName(),
                product.getProductDescription(),
                product.getListPrice(),
                product.getUnitCost(),
                product.getProductId());
    }

    @Override
    public void deleteProduct(Integer productId) {

        jdbcTemplate.update(DELETE_PRODUCT,productId);
    }
}
