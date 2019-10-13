package com.trilogyed.productservice.controller;

import com.trilogyed.productservice.exception.NotFoundException;
import com.trilogyed.productservice.model.Product;
import com.trilogyed.productservice.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@CacheConfig(cacheNames = {"productController"})
public class ProductServiceController {

    @Autowired
    ServiceLayer sl;

    @CachePut(key = "#result.getProductId()")
    @PostMapping(value = "/product")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody @Valid Product product)
    {
        return sl.createProduct(product);
    }

    @GetMapping(value = "/product")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> findAllProducts()
    {
        List<Product> productList = sl.findAllProducts();
        return productList;
    }

    @Cacheable
    @GetMapping(value = "/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public Product findProductByProductId(@PathVariable Integer productId)
    {
        Product product = sl.findProduct(productId);
        if (product==null) throw new NotFoundException("No product exists with this id.");
        return product;
    }

    @CacheEvict(key = "#product.getProductId()")
    @PutMapping(value = "/product")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateProduct(@RequestBody @Valid Product product)
    {
        if(sl.findProduct(product.getProductId())==null) throw new NotFoundException("No product exist with this id");
        sl.updateProduct(product);
    }

    @CacheEvict
    @DeleteMapping(value = "/product/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteProduct(@PathVariable Integer productId)
    {
        if (sl.findProduct(productId)==null) throw new NotFoundException("Cannot find product with this id.");
        sl.deleteProduct(productId);
    }

}
