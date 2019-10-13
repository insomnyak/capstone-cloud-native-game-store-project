package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.InvoiceViewModel;
import com.trilogyed.retailapiservice.exception.InvoiceServiceUnavailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    private InvoiceViewModel ivm = new InvoiceViewModel() {{
        setInvoiceId(-1);
        setCustomerId(-1);
    }};
    private List<InvoiceViewModel> ivmList = new ArrayList<InvoiceViewModel>() {{
        add(ivm);
    }};

    public InvoiceViewModel saveInvoiceViewModel(@Valid InvoiceViewModel ivm) {
        return ivm;
    }

    public InvoiceViewModel findInvoiceViewModelByInvoiceId(Integer invoiceId) {
        return ivm;
    }

    public List<InvoiceViewModel> findAllInvoiceViewModels() {
        return ivmList;
    }

    public List<InvoiceViewModel> findInvoiceViewModelsByCustomerId(Integer customerId) {
        return ivmList;
    }

    public Integer countInvoiceByInvoiceId(Integer invoiceId) {
        return -1;
    }

    public Integer countInvoicesByCustomerId(Integer customerId) {
        return -1;
    }
}