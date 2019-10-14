package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "U2-PRODUCT-SERVICE", configuration = DefaultFeignClientConfiguration.class)
public interface ProductServiceClient {

    @GetMapping("/product")
    public List<Product> findAllProducts();

    @GetMapping(value = "/product/{productId}")
    public Product findProductByProductId(@PathVariable(name = "productId") int productId);
}