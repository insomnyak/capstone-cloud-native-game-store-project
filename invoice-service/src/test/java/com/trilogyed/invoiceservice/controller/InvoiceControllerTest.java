package com.trilogyed.invoiceservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.invoiceservice.model.InvoiceItem;
import com.trilogyed.invoiceservice.service.ServiceLayer;
import com.trilogyed.invoiceservice.viewmodel.InvoiceViewModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(InvoiceController.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ServiceLayer sl;

    @Before
    public void setUp() throws Exception {
        constructSampleData();

    }

    private InvoiceViewModel ivm1a;
    private InvoiceViewModel ivm1b;
    private InvoiceViewModel ivm2a;
    private InvoiceViewModel ivm2b;
    private InvoiceViewModel ivm2c;

    private InvoiceItem invoiceItem1a;
    private InvoiceItem invoiceItem1b;
    private InvoiceItem invoiceItem2a;
    private InvoiceItem invoiceItem2b;
    private InvoiceItem invoiceItem2c;

    private List<InvoiceItem> invoiceItems1a;
    private List<InvoiceItem> invoiceItems1b;
    private List<InvoiceItem> invoiceItems2a;
    private List<InvoiceItem> invoiceItems2b;
    private List<InvoiceItem> invoiceItems2c;

    private List<InvoiceViewModel> ivmList1;
    private List<InvoiceViewModel> ivmList2;

    @After
    public void tearDown() throws Exception {
        destroySampleData();
    }

    public void constructSampleData() {
        ivm1a = new InvoiceViewModel();
        ivm1b = new InvoiceViewModel();
        ivm2a = new InvoiceViewModel();
        ivm2b = new InvoiceViewModel();
        ivm2c = new InvoiceViewModel();

        invoiceItem1a = new InvoiceItem();
        invoiceItem1b = new InvoiceItem();
        invoiceItem2a = new InvoiceItem();
        invoiceItem2b = new InvoiceItem();
        invoiceItem2c = new InvoiceItem();

        invoiceItems1a = new ArrayList<>();
        invoiceItems1b = new ArrayList<>();
        invoiceItems2a = new ArrayList<>();
        invoiceItems2b = new ArrayList<>();
        invoiceItems2c = new ArrayList<>();

        ivmList1 = new ArrayList<>();
        ivmList2 = new ArrayList<>();

        // ivm1 request
        ivm1a.setPurchaseDate(LocalDate.parse("2019-01-01"));
        ivm1a.setCustomerId(10);
        invoiceItem1a.setUnitPrice(new BigDecimal("4.99"));
        invoiceItem1a.setQuantity(44);
        invoiceItem1a.setInventoryId(11);
        invoiceItems1a.add(invoiceItem1a);
        ivm1a.setInvoiceItems(invoiceItems1a);

        // ivm1 response
        ivm1b.setPurchaseDate(LocalDate.parse("2019-01-01"));
        ivm1b.setCustomerId(10);
        invoiceItem1b.setUnitPrice(new BigDecimal("4.99"));
        invoiceItem1b.setQuantity(44);
        invoiceItem1b.setInventoryId(11);
        invoiceItem1b.setInvoiceId(1);
        invoiceItem1b.setInvoiceItemId(1);
        invoiceItems1b.add(invoiceItem1b);
        ivm1b.setInvoiceItems(invoiceItems1b);
        ivm1b.setInvoiceId(1);

        // ivmList1 && findAll
        ivmList1.add(ivm1b);

        // ivm2 request
        ivm2a.setPurchaseDate(LocalDate.parse("2019-02-02"));
        ivm2a.setCustomerId(20);
        invoiceItem2a.setUnitPrice(new BigDecimal("5.99"));
        invoiceItem2a.setQuantity(44);
        invoiceItem2a.setInventoryId(22);
        invoiceItems2a.add(invoiceItem2a);
        ivm2a.setInvoiceItems(invoiceItems2a);

        // ivm2 response
        ivm2b.setPurchaseDate(LocalDate.parse("2019-02-02"));
        ivm2b.setCustomerId(20);
        invoiceItem2b.setUnitPrice(new BigDecimal("5.99"));
        invoiceItem2b.setQuantity(44);
        invoiceItem2b.setInventoryId(22);
        invoiceItem2b.setInvoiceId(2);
        invoiceItem2b.setInvoiceItemId(2);
        invoiceItems2b.add(invoiceItem2b);
        ivm2b.setInvoiceItems(invoiceItems2b);
        ivm2b.setInvoiceId(2);

        // ivm2 update response
        ivm2c.setPurchaseDate(LocalDate.parse("2019-02-02"));
        ivm2c.setCustomerId(20);
        invoiceItem2c.setUnitPrice(new BigDecimal("5.99"));
        invoiceItem2c.setQuantity(66);
        invoiceItem2c.setInventoryId(22);
        invoiceItem2c.setInvoiceId(2);
        invoiceItem2c.setInvoiceItemId(2);
        invoiceItems2c.add(invoiceItem2c);
        ivm2c.setInvoiceItems(invoiceItems2c);
        ivm2c.setInvoiceId(2);

        // ivmList2
        ivmList2.add(ivm2c);

        // mocks
        when(sl.save(ivm1a)).thenReturn(ivm1b);
        when(sl.findInvoiceViewModelByInvoiceId(1)).thenReturn(ivm1b);
        when(sl.findAllInvoiceViewModels()).thenReturn(ivmList1);
        when(sl.findInvoiceViewModelsByCustomerId(10)).thenReturn(ivmList1);
        when(sl.countInvoiceByInvoiceId(1)).thenReturn(1);
        when((sl.countInvoicesByCustomerId(10))).thenReturn(1);

        when(sl.save(ivm2a)).thenReturn(ivm2b);
        doNothing().when(sl).update(ivm2c);
        when(sl.findInvoiceViewModelByInvoiceId(2)).thenReturn(ivm2c);
        when(sl.findInvoiceViewModelsByCustomerId(20)).thenReturn(ivmList2);
        when(sl.countInvoiceByInvoiceId(2)).thenReturn(1);
        when(sl.countInvoicesByCustomerId(20)).thenReturn(1);

        doNothing().when(sl).deleteInvoiceViewModelByInvoiceId(5);
        when(sl.findInvoiceViewModelByInvoiceId(5)).thenReturn(null);
        when(sl.findInvoiceViewModelsByCustomerId(50)).thenReturn(new ArrayList<>());
        when(sl.countInvoiceByInvoiceId(5)).thenReturn(0);
        when(sl.countInvoicesByCustomerId(50)).thenReturn(0);

    }

    public void destroySampleData() {
        ivm1a = null;
        ivm1b = null;
        ivm2a = null;
        ivm2b = null;
        ivm2c = null;

        invoiceItem1a = null;
        invoiceItem1b = null;
        invoiceItem2a = null;
        invoiceItem2b = null;
        invoiceItem2c = null;

        invoiceItems1a = null;
        invoiceItems1b = null;
        invoiceItems2a = null;
        invoiceItems2b = null;
        invoiceItems2c = null;

        ivmList1 = null;
        ivmList2 = null;
    }

    @Test
    public void saveInvoiceViewModel() throws Exception {
        String jsonInput = mapper.writeValueAsString(ivm1a);
        String expectJsonOutput = mapper.writeValueAsString(ivm1b);

        // save & verify output
        this.mockMvc.perform(post("/invoice").content(jsonInput)
                .characterEncoding("UTF-8")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(expectJsonOutput));
    }

    @Test
    public void updateInvoiceViewModel() throws Exception {
        String jsonInput = mapper.writeValueAsString(ivm2a);
        String expectJsonOutput = mapper.writeValueAsString(ivm2b);

        // save
        this.mockMvc.perform(post("/invoice").content(jsonInput)
                .characterEncoding("UTF-8")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(expectJsonOutput));

        String updateJsonInput = mapper.writeValueAsString(ivm2c);

        // update
        this.mockMvc.perform(put("/invoice").content(updateJsonInput)
                .characterEncoding("UTF-8")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isAccepted());

        String updateExpectedJson = mapper.writeValueAsString(ivm2c);

        // get
        this.mockMvc.perform(get("/invoice/{invoiceId}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(updateExpectedJson));
    }

    @Test
    public void deleteInvoiceViewModelByInvoiceId() throws Exception {
        this.mockMvc.perform(delete("/invoice/{invoiceId}", 5))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void findInvoiceViewModelByInvoiceId() throws Exception {
        String expectedJsonOutput = mapper.writeValueAsString(ivm1b);

        this.mockMvc.perform(get("/invoice/{invoiceId}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonOutput));
    }

    @Test
    public void findAllInvoiceViewModels() throws Exception {
        String expectedJsonOutput = mapper.writeValueAsString(ivmList1);

        this.mockMvc.perform(get("/invoice"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonOutput));
    }

    @Test
    public void findInvoiceViewModelsByCustomerId() throws Exception {
        String expectedJsonOutput = mapper.writeValueAsString(ivmList1);

        this.mockMvc.perform(get("/invoice/customer/{customerId}", 10))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonOutput));
    }

    @Test
    public void countInvoiceByInvoiceId() throws Exception {
        this.mockMvc.perform(get("/invoice/{invoiceId}/count", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("1"));

        this.mockMvc.perform(get("/invoice/{invoiceId}/count", 5))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("0"));
    }

    @Test
    public void countInvoicesByCustomerId() throws Exception {
        this.mockMvc.perform(get("/invoice/customer/{customerId}/count", 10))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("1"));

        this.mockMvc.perform(get("/invoice/customer/{customerId}/count", 50))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("0"));
    }
}