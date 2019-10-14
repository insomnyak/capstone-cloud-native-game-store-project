package com.trilogyed.adminapi.controllermockmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.adminapi.controller.AdminAPIController;
import com.trilogyed.adminapi.domain.CustomerViewModel;
import com.trilogyed.adminapi.domain.InvoiceViewModel;
import com.trilogyed.adminapi.domain.TotalInvoiceViewModel;
import com.trilogyed.adminapi.model.Customer;
import com.trilogyed.adminapi.model.LevelUp;
import com.trilogyed.adminapi.service.CustomerServiceLayer;
import com.trilogyed.adminapi.service.InvoiceServiceLayer;
import com.trilogyed.adminapi.service.ProductServiceLayer;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminAPIController.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class ControllerMockMvc {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper mapper;
//
//    @MockBean
//    private CustomerServiceLayer csl;
//
//    @MockBean
//    private InvoiceServiceLayer isl;
//
//    @MockBean
//    private ProductServiceLayer psl;
//
//    @Before
//    public void setUp() throws Exception {
//        //constructSampleData();
//    }
//
//    private Customer requestCreateCustomer;
//    private CustomerViewModel responseCustomer;
//    private CustomerViewModel additionalCustomer;
//    private List<CustomerViewModel> customerList;
//    private CustomerViewModel updateCustomer;
//    private LevelUp updateLevelUp;
//    private CustomerViewModel deleteCustomer;
//
//    private InvoiceViewModel requestIVM;
//    private TotalInvoiceViewModel responseTIVM;
//
//    private InvoiceViewModel requestUpdateIVM;


}
