package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "U2-product-service")
@RequestMapping("/product")
public interface ProductServiceClient {
    @PostMapping
    public Product createProduct(@RequestBody @Valid Product product);

    @GetMapping
    public List<Product> findAllProducts();

    @GetMapping(value = "/{productId}")
    public Product findProductByProductId(@PathVariable(name = "productId") int productId);

    @PutMapping
    public void updateProduct(@RequestBody @Valid Product product);

    @DeleteMapping(value = "/{productId}")
    public void deleteProduct(@PathVariable(name = "productId") int productId);
}
