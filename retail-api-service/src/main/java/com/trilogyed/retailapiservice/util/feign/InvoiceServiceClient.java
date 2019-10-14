package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.InvoiceViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "U2-invoice-service", configuration = DefaultFeignClientConfiguration.class)
public interface InvoiceServiceClient {

    @PostMapping("/invoice")
    public InvoiceViewModel saveInvoiceViewModel(@RequestBody @Valid InvoiceViewModel ivm);

    @GetMapping("/invoice/{invoiceId}")
    public InvoiceViewModel findInvoiceViewModelByInvoiceId(@PathVariable Integer invoiceId);

    @GetMapping("/invoice")
    public List<InvoiceViewModel> findAllInvoiceViewModels();

    @GetMapping("/invoice/customer/{customerId}")
    public List<InvoiceViewModel> findInvoiceViewModelsByCustomerId(@PathVariable Integer customerId);

    @GetMapping("/invoice/{invoiceId}/count")
    public Integer countInvoiceByInvoiceId(@PathVariable Integer invoiceId);

    @GetMapping("/invoice/customer/{customerId}/count")
    public Integer countInvoicesByCustomerId(@PathVariable Integer customerId);

}