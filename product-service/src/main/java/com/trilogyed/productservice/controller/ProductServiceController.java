package com.trilogyed.productservice.controller;

import com.trilogyed.productservice.dao.ProductDao;
import com.trilogyed.productservice.exception.NotFoundException;
import com.trilogyed.productservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
public class ProductServiceController {

    @Autowired
    ProductDao dao;

    @PostMapping(value = "/product")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody @Valid Product product)
    {
        return dao.addProduct(product);
    }

    @GetMapping(value = "/product")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> findAllProducts()
    {
        List<Product> productList = dao.getAllProducts();
        return productList;
    }

    @GetMapping(value = "/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public Product findProductByProductId(@PathVariable(name = "productId") int productId)
    {
        Product product = dao.getProductByProductId(productId);
        if (product==null) throw new NotFoundException("No product exists with this id.");
        return product;
    }

    @PutMapping(value = "/product/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateProduct(@RequestBody @Valid Product product, @PathVariable(name = "productId") int productId)
    {
        if(product.getProductId()==0) product.setProductId(productId);
        if(product.getProductId()!=productId) throw new NotFoundException("No product exist with this id");
        dao.updateProduct(product);
    }

    @DeleteMapping(value = "/product/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteProduct(@PathVariable(name = "productId") int productId)
    {
        if (dao.getProductByProductId(productId)==null) throw new NotFoundException("Cannot find product with this id.");
        dao.deleteProduct(productId);
    }

}
