package com.trilogyed.productservice.service;

import com.trilogyed.productservice.dao.ProductDao;
import com.trilogyed.productservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ServiceLayer {
    private ProductDao dao;

    @Autowired
    public ServiceLayer(ProductDao dao) {
        this.dao = dao;
    }

    @Transactional
    public Product createProduct(Product product)
    {
        return dao.addProduct(product);
    }

    public Product findProduct(int productId)
    {
        return dao.getProductByProductId(productId);
    }

    public List<Product> findAllProducts()
    {
        return dao.getAllProducts();
    }

    public void updateProduct(Product product)
    {
        dao.updateProduct(product);
    }

    public void deleteProduct(int productId)
    {
        dao.deleteProduct(productId);
    }

}
