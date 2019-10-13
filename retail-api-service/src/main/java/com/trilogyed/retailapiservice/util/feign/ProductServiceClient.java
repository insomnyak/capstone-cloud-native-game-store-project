package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.Product;
//import org.springframework.cloud.openfeign.FeignClient;
import com.trilogyed.retailapiservice.exception.ProductServiceUnavailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "U2-PRODUCT-SERVICE", fallback = ProductServiceClientFallback.class)
public interface ProductServiceClient {

    @GetMapping("/product")
    public List<Product> findAllProducts();

    @GetMapping(value = "/product/{productId}")
    public Product findProductByProductId(@PathVariable(name = "productId") int productId);
}

@Component
class ProductServiceClientFallback implements ProductServiceClient {

    public List<Product> findAllProducts() {
        throw new ProductServiceUnavailableException("Products cannot be retrieved at the moment.");
    }

    public Product findProductByProductId(int productId) {
        throw new ProductServiceUnavailableException("Products cannot be retrieved at the moment.");
    }
}
