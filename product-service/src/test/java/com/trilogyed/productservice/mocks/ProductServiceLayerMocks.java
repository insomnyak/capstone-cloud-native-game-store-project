package com.trilogyed.productservice.mocks;

import com.trilogyed.productservice.dao.ProductDao;
import com.trilogyed.productservice.model.Product;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

public class ProductServiceLayerMocks {
    private ProductDao dao;

    @Test
    public void addGetUpdateDeleteProduct() {
        dao = mock(ProductDao.class);

        Product product = new Product();
        product.setProductName("Lamy");
        product.setProductDescription("Fountain Pen");
        product.setListPrice(new BigDecimal("2.49"));
        product.setUnitCost(new BigDecimal("1.49"));

        Product savedProduct = new Product();
        savedProduct.setProductId(100);
        savedProduct.setProductName("Lamy");
        savedProduct.setProductDescription("Fountain Pen");
        savedProduct.setListPrice(new BigDecimal("2.49"));
        savedProduct.setUnitCost(new BigDecimal("1.49"));

        Product anotherProduct = new Product();
        anotherProduct.setProductId(200);
        anotherProduct.setProductName("Lamy Elite");
        anotherProduct.setProductDescription("Fountain Pen");
        anotherProduct.setListPrice(new BigDecimal("2.49"));
        anotherProduct.setUnitCost(new BigDecimal("1.49"));

        List<Product> productList = new ArrayList<>();
        productList.add(savedProduct);
        productList.add(anotherProduct);

        Product updateProduct = new Product();
        updateProduct.setProductId(300);
        updateProduct.setProductName("Lamy");
        updateProduct.setProductDescription("Fountain Pen");
        updateProduct.setListPrice(new BigDecimal("2.49"));
        updateProduct.setUnitCost(new BigDecimal("1.49"));

        Product deleteProduct = new Product();
        deleteProduct.setProductId(400);
        deleteProduct.setProductName("Lamy");
        deleteProduct.setProductDescription("Fountain Pen");
        deleteProduct.setListPrice(new BigDecimal("2.49"));
        deleteProduct.setUnitCost(new BigDecimal("1.49"));

        doReturn(savedProduct).when(dao).addProduct(product);
        doReturn(savedProduct).when(dao).getProductByProductId(100);
        doReturn(productList).when(dao).getAllProducts();
        doReturn(anotherProduct).when(dao).getProductByProductId(200);
        doNothing().when(dao).updateProduct(updateProduct);
        doReturn(updateProduct).when(dao).getProductByProductId(300);
        doNothing().when(dao).deleteProduct(400);
        doReturn(null).when(dao).getProductByProductId(400);

        /** testing adding a new product with inventory */
        Product product1= dao.addProduct(product);
        Product product2 = dao.getProductByProductId(100);
        assertEquals(product1,product2);

        /** Testing find all Items */
        List<Product> ivmListAllProducts = dao.getAllProducts();
        assertEquals(ivmListAllProducts.size(), 2);

        /** Testing update Item Or Inventory */
        Product updateIvm = dao.getProductByProductId(300);
        dao.updateProduct(updateIvm);
        Product updatedIvm = dao.getProductByProductId(updateIvm.getProductId());
        assertEquals(updatedIvm, updateIvm);

        /** Testing delete Item */
        dao.deleteProduct(400);
        assertNull(dao.getProductByProductId(400));
    }
}
