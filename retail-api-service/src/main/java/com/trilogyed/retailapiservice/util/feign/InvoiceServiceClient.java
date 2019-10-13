package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.InvoiceViewModel;
import com.trilogyed.retailapiservice.exception.InvoiceServiceUnavailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "U2-invoice-service", fallback = InvoiceServiceClientFallback.class)
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

@Component
class InvoiceServiceClientFallback implements InvoiceServiceClient {

    public InvoiceViewModel saveInvoiceViewModel(@Valid InvoiceViewModel ivm) {
        throw invoiceServiceUnavailableException();
    }

    public InvoiceViewModel findInvoiceViewModelByInvoiceId(Integer invoiceId) {
        throw invoiceServiceUnavailableException();
    }

    public List<InvoiceViewModel> findAllInvoiceViewModels() {
        throw invoiceServiceUnavailableException();
    }

    public List<InvoiceViewModel> findInvoiceViewModelsByCustomerId(Integer customerId) {
        throw invoiceServiceUnavailableException();
    }

    public Integer countInvoiceByInvoiceId(Integer invoiceId) {
        throw invoiceServiceUnavailableException();
    }

    public Integer countInvoicesByCustomerId(Integer customerId) {
        throw invoiceServiceUnavailableException();
    }

    private InvoiceServiceUnavailableException invoiceServiceUnavailableException() {
        return new InvoiceServiceUnavailableException("Invoices cannot be processed or retrieved at the moment.");
    }
}