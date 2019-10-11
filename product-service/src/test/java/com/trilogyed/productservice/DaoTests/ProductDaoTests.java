package com.trilogyed.productservice.DaoTests;

import com.trilogyed.productservice.dao.ProductDao;
import com.trilogyed.productservice.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductDaoTests {

    @Autowired
    ProductDao dao;

    @Before
    public void setUp() throws Exception
    {
        List<Product> products = dao.getAllProducts();
        products.stream()
                .forEach(product -> {
                    dao.deleteProduct(product.getProductId());
                });
    }

    @Test
    public void addGetUpdateGetAllDelete()
    {
        /** Add Product Tests */
        Product product = new Product();
        product.setProductName("Lamy");
        product.setProductDescription("Fountain Pen");
        product.setListPrice(new BigDecimal("2.98"));
        product.setUnitCost(new BigDecimal("1.40"));

        product = dao.addProduct(product);
        /** Get Product Tests */
        Product product1 = dao.getProductByProductId(product.getProductId());

        assertEquals(product,product1);

        /** Update Product Tests */
        product.setProductDescription("Fountain Pen 0.5mm");
        dao.updateProduct(product);

        product1 = dao.getProductByProductId(product.getProductId());

        assertEquals(product,product1);

        /** Get All Products Tests */
        List<Product> productList = dao.getAllProducts();
        assertEquals(productList.size(),1);

        /** Delete Product Tests */

        dao.deleteProduct(product.getProductId());
        product1 = dao.getProductByProductId(product.getProductId());
        assertNull(product1);
    }

    @Test
    public void findConstraints()
    {
        /** Tests null product name */
        try
        {
            Product product = new Product();
            product.setProductDescription("Fountain Pen");
            product.setListPrice(new BigDecimal("2.98"));
            product.setUnitCost(new BigDecimal("1.40"));

            product = dao.addProduct(product);
        }catch (Exception e)
        {

        }

        /** Tests null product description */
        try
        {
            Product product = new Product();
            product.setProductName("Lamy");
            product.setListPrice(new BigDecimal("2.98"));
            product.setUnitCost(new BigDecimal("1.40"));

            product = dao.addProduct(product);
        }catch (Exception e)
        {

        }

        /** Tests null List Price */
        try
        {
            Product product = new Product();
            product.setProductName("Lamy");
            product.setProductDescription("Fountain Pen");
            product.setUnitCost(new BigDecimal("1.40"));

            product = dao.addProduct(product);
        }catch (Exception e)
        {

        }

        /** Tests null Unit Cost */
        try
        {
            Product product = new Product();
            product.setProductName("Lamy");
            product.setProductDescription("Fountain Pen");
            product.setListPrice(new BigDecimal("2.98"));

            product = dao.addProduct(product);
        }catch (Exception e)
        {

        }

        /** Tests more than allocated integers */
        try
        {
            Product product = new Product();
            product.setProductName("Lamy");
            product.setProductDescription("Fountain Pen");
            product.setListPrice(new BigDecimal("234554.98"));
            product.setUnitCost(new BigDecimal("1.40"));

            product = dao.addProduct(product);
        }catch (Exception e)
        {

        }

        /** Tests more than allocated decimals */
        try
        {
            Product product = new Product();
            product.setProductName("Lamy");
            product.setProductDescription("Fountain Pen");
            product.setListPrice(new BigDecimal("2.98"));
            product.setUnitCost(new BigDecimal("1.4568"));

            product = dao.addProduct(product);
        }catch (Exception e)
        {

        }
    }
}
