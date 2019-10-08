package com.trilogyed.adminapi.controller;

import com.trilogyed.adminapi.domain.InvoiceViewModel;
import com.trilogyed.adminapi.domain.ItemViewModel;
import com.trilogyed.adminapi.model.Customer;
import com.trilogyed.adminapi.model.Inventory;
import com.trilogyed.adminapi.model.LevelUp;
import com.trilogyed.adminapi.model.Product;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
public class AdminAPIController {

    @PostMapping(value = "/levelUp")
    @ResponseStatus(HttpStatus.CREATED)
    public LevelUp createLevelUp(@RequestBody @Valid LevelUp levelUp)
    {
        return null;
    }

    @GetMapping(value = "/levelUp")
    @ResponseStatus(HttpStatus.OK)
    public List<LevelUp> findAllLevelUps()
    {
        return null;
    }

    @GetMapping(value = "/levelUp/{levelUpId}")
    @ResponseStatus(HttpStatus.OK)
    public LevelUp findLevelUpByLevelUpId(@PathVariable(name = "levelUpId") int levelUpId)
    {
        return null;
    }

    @GetMapping(value = "/levelUp/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<LevelUp> findAllLevelUpByCustomerId(@PathVariable(name = "customerId") int customerId)
    {
        return null;
    }

    @PutMapping(value = "/levelUp/{levelUpId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateLevelUp(@RequestBody @Valid LevelUp levelUp, @PathVariable(name = "levelUpId") int levelUpId)
    {

    }

    @DeleteMapping(value = "/levelUp/{levelUpId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLevelUp(@PathVariable(name = "levelUpId") int levelUpId)
    {

    }

    @PostMapping(value = "/invoice")
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceViewModel createInvoice(@RequestBody @Valid InvoiceViewModel ivm)
    {
        return null;
    }

    @GetMapping(value = "/invoice")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceViewModel> getAllInvoices()
    {
        return null;
    }

    @GetMapping(value = "/invoice/{invoiceId}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceViewModel getInvoiceByInvoiceId(@PathVariable(name = "invoiceId")int invoiceId)
    {
        return null;
    }

    @GetMapping(value = "/invoice/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceViewModel> getInvoicesByCustomerId(@PathVariable(name = "customerId") int customerId)
    {
        return null;
    }

    @PutMapping(value = "/invoice/{invoiceId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateInvoice(@RequestBody @Valid InvoiceViewModel ivm,@PathVariable(name = "invoiceId") int invoiceId)
    {

    }

    @DeleteMapping(value = "/invoice/{invoiceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable(name = "invoiceId") int invoiceId)
    {

    }

    @PostMapping(value = "/inventory")
    @ResponseStatus(HttpStatus.CREATED)
    public Inventory createInventory(@RequestBody @Valid Inventory inventory)
    {
        return null;
    }

    @GetMapping(value = "/inventory")
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> getAllInventory()
    {
        return null;
    }

    @GetMapping(value = "/inventory/{inventoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Inventory getInventoryByInventoryId(@PathVariable (name = "inventoryId") int inventoryId)
    {
        return null;
    }

    @PutMapping(value = "/inventory/{inventoryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateInventory(@RequestBody @Valid Inventory inventory, @PathVariable(name = "inventoryId") int inventoryId)
    {

    }

    @DeleteMapping(value = "/inventory/{inventoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInventory(@PathVariable(name = "inventoryId") int inventoryId)
    {

    }

    @PostMapping(value = "/product")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemViewModel createProduct(@RequestBody @Valid ItemViewModel ivm)
    {
        return null;
    }

    @GetMapping(value = "/product")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemViewModel> findAllProducts()
    {
        return null;
    }

    @GetMapping(value = "/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemViewModel findProductByProductId(@PathVariable(name = "productId") int productId)
    {
        return null;
    }

    @GetMapping(value = "/product/invoice/{invoiceId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemViewModel> getProductsByInvoiceId(@PathVariable(name = "invoiceId")int invoiceId)
    {
        return null;
    }

    @GetMapping(value = "/product/inventory")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemViewModel> getProductsByInventory()
    {
        return null;
    }

    @PutMapping(value = "/product/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateProduct(@RequestBody @Valid ItemViewModel ivm, @PathVariable(name = "productId") int productId)
    {

    }

    @DeleteMapping(value = "/product/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteProduct(@PathVariable(name = "productId") int productId)
    {

    }

    @PostMapping(value = "/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@RequestBody @Valid Customer customer)
    {
        return null;
    }

    @GetMapping(value = "/customer")
    @ResponseStatus(HttpStatus.OK)
    public List<Customer> getAllCustomers()
    {
        return null;
    }

    @GetMapping(value = "/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public Customer getCustomerByCustomerId(@PathVariable(name = "customerId") int customerId)
    {
        return null;
    }

    @PutMapping(value = "/customer/{customerId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCustomer(@RequestBody @Valid Customer customer, @PathVariable(name = "customerId") int customerId)
    {

    }

    @DeleteMapping(value = "/customer/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable(name = "customerId") int customerId)
    {

    }


}
