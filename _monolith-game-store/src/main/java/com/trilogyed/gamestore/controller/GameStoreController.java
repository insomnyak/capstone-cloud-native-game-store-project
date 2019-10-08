package com.trilogyed.gamestore.controller;

import com.trilogyed.gamestore.model.Invoice;
import com.trilogyed.gamestore.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GameStoreController {

    // Retail Endpoints

    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    public Invoice submitInvoice(@RequestBody Invoice invoice) {
        return null;
    }

    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.GET)
    public Invoice getInvoiceById(@PathVariable int id) {
        return null;
    }

    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    public List<Invoice> getAllInvoices() {
        return null;
    }

    @RequestMapping(value = "/invoices/customer/{id}", method = RequestMethod.GET)
    public List<Invoice> getInvoicesByCustomerId(@PathVariable int id) {
        return null;
    }

    @RequestMapping(value = "/products/inventory", method = RequestMethod.GET)
    public List<Product> getProductsInInventory() {
        return null;
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public Product getProductById(@PathVariable int id) {
        return null;
    }

    @RequestMapping(value = "/products/invoice/{id}", method = RequestMethod.GET)
    public List<Product> getProductByInvoiceId(@PathVariable int id) {
        return null;
    }

    @RequestMapping(value = "/levelup/customer/{id}", method = RequestMethod.GET)
    public int getLevelUpPointsByCustomerId(int id) {
        return 0;
    }

    // Admin Endpoints

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public Product createProduct(@RequestBody Product product) {
        return null;
    }

    // getProductById is above in the Retail endpoints

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> getAllProducts() {
        return null;
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    public void updateProduct(@PathVariable int id, @RequestBody Product product) {

    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable int id) {

    }

}
