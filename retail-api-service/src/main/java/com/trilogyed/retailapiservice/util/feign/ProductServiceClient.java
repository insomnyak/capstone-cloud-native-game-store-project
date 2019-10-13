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
    private Product product = new Product() {{
        setProductId(-1);
    }};
    private List<Product> products = new ArrayList<Product>() {{
        add(product);
    }};

    public List<Product> findAllProducts() {
        return products;
    }

    public Product findProductByProductId(int productId) {
        return product;
    }
}
