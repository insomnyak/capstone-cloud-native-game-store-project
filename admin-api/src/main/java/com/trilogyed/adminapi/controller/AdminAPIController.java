package com.trilogyed.adminapi.controller;

import com.trilogyed.adminapi.domain.*;
import com.trilogyed.adminapi.exception.NotFoundException;
import com.trilogyed.adminapi.model.Customer;
import com.trilogyed.adminapi.model.Inventory;
import com.trilogyed.adminapi.model.LevelUp;
import com.trilogyed.adminapi.service.CustomerServiceLayer;
import com.trilogyed.adminapi.service.InvoiceServiceLayer;
import com.trilogyed.adminapi.service.ProductServiceLayer;
import com.trilogyed.adminapi.util.feign.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@CacheConfig(cacheNames = {"adminApi"})
@RequestMapping(value = "/admin")
public class AdminAPIController {

    @Autowired
    CustomerServiceLayer csl;

    @Autowired
    InvoiceServiceLayer isl;

    @Autowired
    ProductServiceLayer psl;

    private LevelUpServiceClient levelUpClient;
    private CustomerServiceClient customerClient;
    private InventoryServiceClient inventoryClient;
    private InvoiceServiceClient invoiceClient;
    private ProductServiceClient productClient;

    @CachePut(key = "#result.getLevelUpId()")
    @PostMapping(value = "/levelUp")
    @ResponseStatus(HttpStatus.CREATED)
    public LevelUp createLevelUp(@RequestBody @Valid LevelUp levelUp)
    {
        return levelUpClient.createLevelUp(levelUp) ;
    }

    @GetMapping(value = "/levelUp")
    @ResponseStatus(HttpStatus.OK)
    public List<LevelUp> findAllLevelUps()
    {
        return levelUpClient.findAllLevelUps();
    }

    @Cacheable
    @GetMapping(value = "/levelUp/{levelUpId}")
    @ResponseStatus(HttpStatus.OK)
    public LevelUp findLevelUpByLevelUpId(@PathVariable(name = "levelUpId") Integer levelUpId)
    {
        LevelUp levelUp = levelUpClient.findByLevelUpId(levelUpId);
        if (levelUp==null) throw new NotFoundException("No levelUp exists in the database with this id");
        return levelUp;
    }

    @Cacheable
    @GetMapping(value = "/levelUp/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<LevelUp> findAllLevelUpByCustomerId(@PathVariable(name = "customerId") Integer customerId)
    {
        List<LevelUp> levelUpList = levelUpClient.findLevelUpsByCustomerId(customerId);
        if(levelUpList==null) throw new NotFoundException("No levelUp exists in the database for this customer");
        return levelUpList;
    }

    @CacheEvict(key = "#levelUp.getLevelUpId()")
    @PutMapping(value = "/levelUp")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateLevelUp(@RequestBody @Valid LevelUp levelUp)
    {
        LevelUp levelUp1 = levelUpClient.findByLevelUpId(levelUp.getLevelUpId());
        if (levelUp1==null) throw new NotFoundException("Cannot find levelUp with this levelUpId");
        levelUpClient.updateLevelUp(levelUp);
    }

    @CacheEvict
    @DeleteMapping(value = "/levelUp/{levelUpId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLevelUp(@PathVariable(name = "levelUpId") Integer levelUpId)
    {
        LevelUp levelUp1 = levelUpClient.findByLevelUpId(levelUpId);
        if(levelUp1==null) throw new NotFoundException("Cannot find LevelUp with this id");
        levelUpClient.deleteByLevelUpId(levelUpId);
    }

    @CachePut(key = "#result.getInvoiceId()")
    @PostMapping(value = "/invoice",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TotalInvoiceViewModel createInvoice(@RequestBody @Valid InvoiceViewModel ivm)
    {
        return isl.createInvoice(ivm);
    }

    @GetMapping(value = "/invoice")
    @ResponseStatus(HttpStatus.OK)
    public List<TotalInvoiceViewModel> getAllInvoices()
    {
        return isl.getAllInvoices();
    }

    @Cacheable
    @GetMapping(value = "/invoice/{invoiceId}")
    @ResponseStatus(HttpStatus.OK)
    public TotalInvoiceViewModel getInvoiceByInvoiceId(@PathVariable(name = "invoiceId")Integer invoiceId)
    {
        TotalInvoiceViewModel tivm =  isl.getInvoice(invoiceId);
        if(tivm==null) throw new NotFoundException("No invoice exists with this id");
        return tivm;
    }

    @Cacheable
    @GetMapping(value = "/invoice/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TotalInvoiceViewModel> getInvoicesByCustomerId(@PathVariable(name = "customerId") Integer customerId)
    {
        List<TotalInvoiceViewModel> tivmList = isl.getInvoicesByCustomerId(customerId);
        if(tivmList==null) throw new NotFoundException("No invoices exists for this customer id");
        return tivmList;
    }

    @CacheEvict(key = "#invoice.getInvoiceId()")
    @PutMapping(value = "/invoice")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateInvoice(@RequestBody @Valid TotalInvoiceViewModel tivm)
    {
        TotalInvoiceViewModel tivm1 = isl.getInvoice(tivm.getInvoiceId());
        if(tivm1==null) throw new NotFoundException("No such invoice exists");
        isl.updateInvoiceIncludingInvoiceItems(tivm);
    }

    @CacheEvict
    @DeleteMapping(value = "/invoice/{invoiceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable(name = "invoiceId") Integer invoiceId)
    {
        TotalInvoiceViewModel tivm = isl.getInvoice(invoiceId);
        if (tivm==null) throw new NotFoundException("No such invoice exists");
        isl.deleteInvoice(invoiceId);
    }

    @CachePut(key = "#result.getInventoryId()")
    @PostMapping(value = "/inventory")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemViewModel createInventory(@RequestBody @Valid Inventory inventory)
    {
        return psl.createInventory(inventory);
    }

    @GetMapping(value = "/inventory")
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> getAllInventory()
    {
        return inventoryClient.findAllInventories();
    }

    @Cacheable
    @GetMapping(value = "/inventory/{inventoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Inventory getInventoryByInventoryId(@PathVariable (name = "inventoryId") Integer inventoryId)
    {
        Inventory inventory = inventoryClient.findInventoryByInventoryId(inventoryId);
        if(inventory==null) throw new NotFoundException("No inventory exists with this id");
        return inventory;
    }

    @CacheEvict(key = "#inventory.getInventoryId()")
    @PutMapping(value = "/inventory")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateInventory(@RequestBody @Valid ItemViewModel ivm)
    {
        Inventory inventory1 = inventoryClient.findInventoryByInventoryId(ivm.getInventoryId());
        if(inventory1==null) throw new NotFoundException("No such inventory exists");
        psl.updateInventory(ivm);
    }

    @CacheEvict
    @DeleteMapping(value = "/inventory/{inventoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInventory(@PathVariable(name = "inventoryId") Integer inventoryId)
    {
        Inventory inventory1 = inventoryClient.findInventoryByInventoryId(inventoryId);
        if(inventory1==null) throw new NotFoundException("No such inventory exists");
        inventoryClient.deleteInventory(inventoryId);
    }

    @CachePut(key = "#result.getProductId()")
    @PostMapping(value = "/product")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemViewModel createProductViewModel(@RequestBody @Valid ItemViewModel ivm)
    {
        return psl.createItem(ivm);
    }

    @GetMapping(value = "/product")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemViewModel> findAllProducts()
    {
        System.out.println("Checking...");
        return psl.findAllItems();
    }

    @Cacheable
    @GetMapping(value = "/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemViewModel findProductByProductId(@PathVariable(name = "productId") Integer productId)
    {
        ItemViewModel ivm = psl.findItemByProductId(productId);
        if(ivm==null) throw new NotFoundException("No such product exists with this id");
        return ivm;
    }

    @Cacheable
    @GetMapping(value = "/product/invoice/{invoiceId}")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceItemViewModel> getProductsByInvoiceId(@PathVariable Integer invoiceId)
    {
        List<InvoiceItemViewModel> iivmList = isl.getItemsByInvoiceId(invoiceId);
        return iivmList;
    }

    @Cacheable
    @GetMapping(value = "/product/inventory/{inventoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemViewModel getProductsByInventory(@PathVariable Integer inventoryId)
    {
        ItemViewModel ivm= psl.findItemByInventoryId(inventoryId);
        if(ivm==null) throw new NotFoundException("No product exists with this inventory Id");
        return ivm;
    }

    @CacheEvict(key = "#product.getProductId()")
    @PutMapping(value = "/product")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateProduct(@RequestBody @Valid ItemViewModel ivm)
    {
        ItemViewModel ivm1 = psl.findItemByProductId(ivm.getProductId());
        if(ivm1==null) throw new NotFoundException("No such product exists with this productId");
        psl.updateItemOrInventory(ivm);
    }

    @CacheEvict
    @DeleteMapping(value = "/product/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteProduct(@PathVariable(name = "productId") Integer productId)
    {
        ItemViewModel ivm1 = psl.findItemByProductId(productId);
        if(ivm1==null) throw new NotFoundException("No such product exists with this productId");
        psl.deleteItem(productId);
    }

    @CachePut(key = "#result.getCustomerId()")
    @PostMapping(value = "/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerViewModel createCustomerViewModel(@RequestBody @Valid Customer customer)
    {
        return csl.createCustomer(customer);
    }

    @GetMapping(value = "/customer")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerViewModel> getAllCustomers()
    {
        return csl.getAllCustomers();
    }

    @Cacheable
    @GetMapping(value = "/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerViewModel getCustomerByCustomerId(@PathVariable(name = "customerId") Integer customerId)
    {
        CustomerViewModel cvm = csl.getCustomer(customerId);
        if(cvm==null) throw new NotFoundException("The customer with this id does not exist in database.");
        return cvm;
    }

    @CacheEvict(key = "#customer.getCustomerId()")
    @PutMapping(value = "/customer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCustomer(@RequestBody @Valid CustomerViewModel cvm)
    {
        CustomerViewModel cvm1 = csl.getCustomer(cvm.getCustomerId());
        if(cvm1==null) throw new NotFoundException("No such customer exists");
        csl.updateCustomer(cvm);
    }

    @CacheEvict
    @DeleteMapping(value = "/customer/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable(name = "customerId") Integer customerId)
    {
        CustomerViewModel cvm1 = csl.getCustomer(customerId);
        if(cvm1==null) throw new NotFoundException("No such customer exists");
        csl.deleteCustomer(customerId);
    }

    @GetMapping(value = "/logoutSuccess")
    @ResponseStatus(HttpStatus.OK)
    public String viewAllDone()
    {
        return "Logout successful!!!";
    }
}
