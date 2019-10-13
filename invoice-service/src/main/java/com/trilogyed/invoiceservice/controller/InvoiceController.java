package com.trilogyed.invoiceservice.controller;

import com.trilogyed.invoiceservice.service.ServiceLayer;
import com.trilogyed.invoiceservice.viewmodel.InvoiceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/invoice")
@CacheConfig(cacheNames = {"invoiceController"})
public class InvoiceController {

    @Autowired
    ServiceLayer sl;

    @CachePut(key = "#result.getInvoiceId()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceViewModel saveInvoiceViewModel(@RequestBody @Valid InvoiceViewModel ivm) {
        return sl.save(ivm);
    }

    @Caching(evict = {
            @CacheEvict(key = "'ivmId:' + #ivm.getInvoiceId()"),
            @CacheEvict(key = "'countByInvoiceId: ' + #ivm.getInvoiceId()")
    })
    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateInvoiceViewModel(@RequestBody @Valid InvoiceViewModel ivm) {
        sl.update(ivm);
    }

    @Caching(evict = {
            @CacheEvict(key = "'ivmId:' + #invoiceId"),
            @CacheEvict(key = "'countByInvoiceId: ' + #invoiceId")
    })
    @DeleteMapping("/{invoiceId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteInvoiceViewModelByInvoiceId(@PathVariable Integer invoiceId) {
        sl.deleteInvoiceViewModelByInvoiceId(invoiceId);
    }

    @Cacheable(key = "'ivmId:' + #invoiceId", condition = "#result != null")
    @GetMapping("/{invoiceId}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceViewModel findInvoiceViewModelByInvoiceId(@PathVariable Integer invoiceId) {
        return sl.findInvoiceViewModelByInvoiceId(invoiceId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceViewModel> findAllInvoiceViewModels() {
        return sl.findAllInvoiceViewModels();
    }

    @GetMapping("/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceViewModel> findInvoiceViewModelsByCustomerId(@PathVariable Integer customerId) {
        return sl.findInvoiceViewModelsByCustomerId(customerId);
    }

    @GetMapping("/{invoiceId}/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer countInvoiceByInvoiceId(@PathVariable Integer invoiceId) {
        return sl.countInvoiceByInvoiceId(invoiceId);
    }

    @GetMapping("/customer/{customerId}/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer countInvoicesByCustomerId(@PathVariable Integer customerId) {
        return sl.countInvoicesByCustomerId(customerId);
    }
}
