package com.trilogyed.adminapi.util.feign;

import com.trilogyed.adminapi.domain.InvoiceViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "U2-invoice-service")
@RequestMapping("/invoice")
public interface InvoiceServiceClient {
    @PostMapping
    public InvoiceViewModel saveInvoiceViewModel(@RequestBody @Valid InvoiceViewModel ivm);

    @PutMapping
    public void updateInvoiceViewModel(@RequestBody @Valid InvoiceViewModel ivm);

    @DeleteMapping("/{invoiceId}")
    public void deleteInvoiceViewModelByInvoiceId(@PathVariable Integer invoiceId);

    @GetMapping("/{invoiceId}")
    public InvoiceViewModel findInvoiceViewModelByInvoiceId(@PathVariable Integer invoiceId);

    @GetMapping
    public List<InvoiceViewModel> findAllInvoiceViewModels();

    @GetMapping("/customer/{customerId}")
    public List<InvoiceViewModel> findInvoiceViewModelsByCustomerId(@PathVariable Integer customerId);

    @GetMapping("/{invoiceId}/count")
    public Integer countInvoiceByInvoiceId(@PathVariable Integer invoiceId);

    @GetMapping("/customer/{customerId}/count")
    public Integer countInvoicesByCustomerId(@PathVariable Integer customerId);
}
