package com.trilogyed.retailapiservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insomnyak.util.MapClasses;
import com.trilogyed.retailapiservice.domain.*;
import com.trilogyed.retailapiservice.service.ServiceLayer;
import com.trilogyed.retailapiservice.service.ServiceLayerRabbitMqHelper;
import com.trilogyed.retailapiservice.util.feign.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.Silent.class)
@WebMvcTest(RetailApiController.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class RetailApiControllerTest {

//    @SpyBean
//    private MockMvc mockMvc;
//
//    @Spy
//    ObjectMapper mapper;
//
//    @Mock
//    private CustomerServiceClient customerClient;
//
//    @Mock
//    private InventoryServiceClient inventoryClient;
//
//    @Mock
//    private InvoiceServiceClient invoiceClient;
//
//    @Mock
//    private LevelUpServiceClient levelUpClient;
//
//    @Mock
//    private ProductServiceClient productClient;
//
//    @Mock
//    private RabbitTemplate rabbitTemplate;
//
//    @Mock
//    private AsyncRabbitTemplate asyncRabbitTemplate;
//
//    @InjectMocks
//    private ServiceLayerRabbitMqHelper rabbitMqHelper = new ServiceLayerRabbitMqHelper(
//            rabbitTemplate, asyncRabbitTemplate);
//
//    @InjectMocks
//    ServiceLayer sl = new ServiceLayer(customerClient, inventoryClient, invoiceClient,
//            levelUpClient, productClient, rabbitMqHelper);
//
//    private Customer customer1a;
//    private Customer customer1b;
//    private Customer customer2a;
//    private Customer customer2b;
//    private Customer customer2c;
//
//    private Inventory inventory1a;
//    private Inventory inventory1b;
//    private Inventory inventory1c;
//    private Inventory inventory2a;
//    private Inventory inventory2b;
//    private Inventory inventory2c;
//    private Inventory inventory2c2;
//
//    private InvoiceViewModel ivm1a;
//    private InvoiceViewModel ivm1b;
//    private InvoiceViewModel ivm2a;
//    private InvoiceViewModel ivm2b;
//    private InvoiceViewModel ivm2c;
//
//    private InvoiceItem ii1a;
//    private InvoiceItem ii1b;
//    private InvoiceItem ii2a;
//    private InvoiceItem ii2b;
//    private InvoiceItem ii2c;
//    private InvoiceItem ii2a2;
//    private InvoiceItem ii2b2;
//    private InvoiceItem ii2c2;
//
//    private List<InvoiceItem> iiList1a;
//    private List<InvoiceItem> iiList1b;
//    private List<InvoiceItem> iiList2a;
//    private List<InvoiceItem> iiList2b;
//    private List<InvoiceItem> iiList2c;
//
//    private LevelUp levelUp1a;
//    private LevelUp levelUp1b;
//    private LevelUp levelUp2a;
//    private LevelUp levelUp2b;
//    private LevelUp levelUp2c;
//
//    private Product product1a;
//    private Product product1b;
//    private Product product2a;
//    private Product product2b;
//    private Product product2c;
//    private Product product2c2;
//    private Product product4;
//
//    private InvoiceItemViewModel iivm1a;
//    private InvoiceItemViewModel iivm1b;
//    private InvoiceItemViewModel iivm2a;
//    private InvoiceItemViewModel iivm2b;
//    private InvoiceItemViewModel iivm2c;
//    private InvoiceItemViewModel iivm2c2;
//
//    private List<InvoiceItemViewModel> iivmList1a;
//    private List<InvoiceItemViewModel> iivmList1b;
//    private List<InvoiceItemViewModel> iivmList2a;
//    private List<InvoiceItemViewModel> iivmList2b;
//    private List<InvoiceItemViewModel> iivmList2c;
//
//    @Before
//    public void setUp() throws Exception {
//        constructSampleData();
//        setUpCustomerClientMock();
//        setUpInventoryClientMock();
//        setUpInvoiceClientMock();
//        setUpLevelUpClientMock();
//        setUpProductClientMock();
//        setUpRabbitMqHelperMock();
//
//        //doCallRealMethod().when(sl).fetchCustomer(1);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    public void setUpCustomerClientMock() {
//        //customerClient = mock(CustomerServiceClient.class);
//
//        doReturn(customer1b).when(customerClient).getCustomerByCustomerId(1);
//        doReturn(customer2b).when(customerClient).getCustomerByCustomerId(2);
//        doReturn(null).when(customerClient).getCustomerByCustomerId(5);
//
//
//    }
//
//    public void setUpInventoryClientMock() {
//        //inventoryClient = mock(InventoryServiceClient.class);
//
//        List<Inventory> inventories1 = new ArrayList<>();
//        inventories1.add(inventory1b);
//
//        List<Inventory> inventories2 = new ArrayList<>();
//        inventories2.add(inventory2c);
//
//        List<Inventory> inventories22 = new ArrayList<>();
//        inventories22.add(inventory2c2);
//
//        List<Inventory> inventoriesAll = new ArrayList<>();
//        inventoriesAll.add(inventory1b);
//        inventoriesAll.add(inventory2c);
//        inventoriesAll.add(inventory2c2);
//
//        doReturn(inventory1b).when(inventoryClient).findInventoryByInventoryId(1);
//        doReturn(1).when(inventoryClient).countInventoriesByProductId(1);
//        doReturn(1).when(inventoryClient).countInventoryByInventoryId(1);
//        doReturn(inventories1).when(inventoryClient).findInventoriesByProductId(1);
//
//        doNothing().when(inventoryClient).updateInventory(inventory2c);
//        doReturn(inventory2c).when(inventoryClient).findInventoryByInventoryId(2);
//        doReturn(inventories2).when(inventoryClient).findInventoriesByProductId(2);
//        doReturn(1).when(inventoryClient).countInventoryByInventoryId(2);
//        doReturn(1).when(inventoryClient).countInventoriesByProductId(2);
//
//        doNothing().when(inventoryClient).updateInventory(inventory2c2);
//        doReturn(inventory2c2).when(inventoryClient).findInventoryByInventoryId(22);
//        doReturn(inventories22).when(inventoryClient).findInventoriesByProductId(22);
//        doReturn(1).when(inventoryClient).countInventoryByInventoryId(22);
//        doReturn(1).when(inventoryClient).countInventoriesByProductId(22);
//
//        doReturn(inventoriesAll).when(inventoryClient).findAllInventories();
//
//        doReturn(null).when(inventoryClient).findInventoryByInventoryId(5);
//        doReturn(new ArrayList<>()).when(inventoryClient).findInventoriesByProductId(5);
//        doReturn(new ArrayList<>()).when(inventoryClient).findInventoriesByProductId(4);
//    }
//
//    public void setUpInvoiceClientMock() {
//        //invoiceClient = mock(InvoiceServiceClient.class);
//
//        List<InvoiceViewModel> invoices1 = new ArrayList<>();
//        invoices1.add(ivm1b);
//
//        List<InvoiceViewModel> invoices2 = new ArrayList<>();
//        invoices2.add(ivm2c);
//
//        doReturn(ivm1b).when(invoiceClient).saveInvoiceViewModel(ivm1a);
//        doReturn(ivm1b).when(invoiceClient).findInvoiceViewModelByInvoiceId(1);
//        doReturn(1).when(invoiceClient).countInvoiceByInvoiceId(1);
//        doReturn(1).when(invoiceClient).countInvoicesByCustomerId(1);
//        doReturn(invoices1).when(invoiceClient).findAllInvoiceViewModels();
//        doReturn(invoices1).when(invoiceClient).findInvoiceViewModelsByCustomerId(1);
//
//        doReturn(ivm2b).when(invoiceClient).saveInvoiceViewModel(ivm2a);
//        doReturn(ivm2c).when(invoiceClient).findInvoiceViewModelByInvoiceId(2);
//        doReturn(1).when(invoiceClient).countInvoicesByCustomerId(2);
//        doReturn(1).when(invoiceClient).countInvoiceByInvoiceId(2);
//
//        doReturn(null).when(invoiceClient).findInvoiceViewModelByInvoiceId(5);
//        doReturn(new ArrayList<>()).when(invoiceClient).findInvoiceViewModelsByCustomerId(5);
//    }
//
//    public void setUpLevelUpClientMock() {
//        //levelUpClient = mock(LevelUpServiceClient.class);
//
//        List<LevelUp> levelUps1 = new ArrayList<>();
//        levelUps1.add(levelUp1b);
//
//        List<LevelUp> levelUps2 = new ArrayList<>();
//        levelUps2.add(levelUp2c);
//
//        doReturn(levelUps1).when(levelUpClient).findLevelUpsByCustomerId(1);
//        doReturn(levelUps2).when(levelUpClient).findLevelUpsByCustomerId(2);
//        doReturn(new ArrayList<>()).when(levelUpClient).findLevelUpsByCustomerId(5);
//    }
//
//    public void setUpProductClientMock() {
//        //productClient = mock(ProductServiceClient.class);
//
//        List<Product> products = new ArrayList<>();
//        products.add(product1b);
//        products.add(product2c);
//        products.add(product2c2);
//
//        doReturn(product1b).when(productClient).findProductByProductId(1);
//        doReturn(product2c).when(productClient).findProductByProductId(2);
//        doReturn(product2c2).when(productClient).findProductByProductId(22);
//        doReturn(product4).when(productClient).findProductByProductId(4);
//
//        doReturn(products).when(productClient).findAllProducts();
//
//        doReturn(null).when(productClient).findProductByProductId(5);
//    }
//
//    public void setUpRabbitMqHelperMock() {
//        //rabbitMqHelper = mock(ServiceLayerRabbitMqHelper.class);
//
//        //doNothing().when(rabbitMqHelper).updateLevelUp(any());
//        //doNothing().when(rabbitMqHelper).updateLevelUp(null);
//    }
//
//    public void constructSampleData() {
//        // CUSTOMER
//        customer1a = new Customer();
//        customer1b = new Customer();
//        customer2a = new Customer();
//        customer2b = new Customer();
//        customer2c = new Customer();
//
//        customer1a.setCity("NYC");
//        customer1a.setEmail("rene@insomnyak.com");
//        customer1a.setFirstName("Rene");
//        customer1a.setLastName("Serulle");
//        customer1a.setPhone("917-832-1777");
//        customer1a.setStreet("Madison Square Park");
//        customer1a.setZip("10001");
//        customer1a.setCustomerId(1);
//
//        customer1b.setCity("NYC");
//        customer1b.setEmail("rene@insomnyak.com");
//        customer1b.setFirstName("Rene");
//        customer1b.setLastName("Serulle");
//        customer1b.setPhone("917-832-1777");
//        customer1b.setStreet("Madison Square Park");
//        customer1b.setZip("10001");
//        customer1b.setCustomerId(1);
//
//        customer2a.setCity("NYC");
//        customer2a.setEmail("rene@insomnyak-llc.com");
//        customer2a.setFirstName("Rene");
//        customer2a.setLastName("Serulle");
//        customer2a.setPhone("917-832-1777");
//        customer2a.setStreet("Madison Square Park");
//        customer2a.setZip("10001");
//        customer2a.setCustomerId(2);
//
//        customer2b.setCity("NYC");
//        customer2b.setEmail("rene@insomnyak-llc.com");
//        customer2b.setFirstName("Rene");
//        customer2b.setLastName("Serulle");
//        customer2b.setPhone("917-832-1777");
//        customer2b.setStreet("Madison Square Park");
//        customer2b.setZip("10001");
//        customer2b.setCustomerId(2);
//
//        customer2c.setCity("NYC");
//        customer2c.setEmail("rene@insomnyak-llc.com");
//        customer2c.setFirstName("Rene");
//        customer2c.setLastName("Serulle");
//        customer2c.setPhone("917-832-1777");
//        customer2c.setStreet("Madison Square Park");
//        customer2c.setZip("10001");
//        customer2c.setCustomerId(2);
//
//        // PRODUCT
//        product1a = new Product();
//        product1b = new Product();
//        product2a = new Product();
//        product2b = new Product();
//        product2c = new Product();
//        product2c2 = new Product();
//        product4 = new Product();
//
//        product1a.setProductName("MacBook Pro");
//        product1a.setListPrice(new BigDecimal("3000.99"));
//        product1a.setProductDescription("128GB RAM, 50TB, Intel i9 Extreme 5.9 GHz");
//        product1a.setUnitCost(new BigDecimal("2999.99"));
//        product1a.setProductId(1);
//
//        product1b.setProductName("MacBook Pro");
//        product1b.setListPrice(new BigDecimal("3000.99"));
//        product1b.setProductDescription("128GB RAM, 50TB, Intel i9 Extreme 5.9 GHz");
//        product1b.setUnitCost(new BigDecimal("2999.99"));
//        product1b.setProductId(1);
//
//        product2a.setProductName("Nintendo Switch");
//        product2a.setListPrice(new BigDecimal("300.99"));
//        product2a.setProductDescription("gaming console");
//        product2a.setUnitCost(new BigDecimal("249.99"));
//        product2a.setProductId(2);
//
//        product2b.setProductName("Nintendo Switch");
//        product2b.setListPrice(new BigDecimal("300.99"));
//        product2b.setProductDescription("gaming console");
//        product2b.setUnitCost(new BigDecimal("249.99"));
//        product2b.setProductId(2);
//
//        product2c.setProductName("Nintendo Switch");
//        product2c.setListPrice(new BigDecimal("300.99"));
//        product2c.setProductDescription("gaming console");
//        product2c.setUnitCost(new BigDecimal("259.99"));
//        product2c.setProductId(2);
//
//        product2c2.setProductName("Xbox");
//        product2c2.setListPrice(new BigDecimal("300.99"));
//        product2c2.setProductDescription("gaming console");
//        product2c2.setUnitCost(new BigDecimal("259.99"));
//        product2c2.setProductId(22);
//
//        product4.setProductName("PS4");
//        product4.setListPrice(new BigDecimal("300.99"));
//        product4.setProductDescription("gaming console");
//        product4.setUnitCost(new BigDecimal("259.99"));
//        product4.setProductId(4);
//
//        // INVENTORY
//        inventory1a = new Inventory();
//        inventory1b = new Inventory();
//        inventory1c = new Inventory();
//        inventory2a = new Inventory();
//        inventory2b = new Inventory();
//        inventory2c = new Inventory();
//        inventory2c2 = new Inventory();
//
//        inventory1a.setProductId(1);
//        inventory1a.setQuantity(45);
//
//        inventory1b.setProductId(1);
//        inventory1b.setQuantity(45);
//        inventory1b.setInventoryId(1);
//
//        inventory1c.setProductId(1);
//        inventory1c.setQuantity(41);
//        inventory1c.setInventoryId(1);
//
//        inventory2a.setProductId(2);
//        inventory2a.setQuantity(25);
//
//        inventory2b.setProductId(2);
//        inventory2b.setQuantity(25);
//        inventory2b.setInventoryId(2);
//
//        inventory2c.setProductId(2);
//        inventory2c.setQuantity(21);
//        inventory2c.setInventoryId(2);
//
//        inventory2c2.setProductId(22);
//        inventory2c2.setQuantity(21);
//        inventory2c2.setInventoryId(22);
//
//        // INVOICE VIEW MODEL
//        // invoice item
//        ii1a = new InvoiceItem();
//        ii1b = new InvoiceItem();
//        ii2a = new InvoiceItem();
//        ii2b = new InvoiceItem();
//        ii2c = new InvoiceItem();
//        ii2a2 = new InvoiceItem();
//        ii2b2 = new InvoiceItem();
//        ii2c2 = new InvoiceItem();
//
//        ii1a.setInventoryId(1);
//        ii1a.setQuantity(4);
//        ii1a.setUnitPrice(product1b.getUnitCost());
//
//        ii1b.setInventoryId(1);
//        ii1b.setQuantity(4);
//        ii1b.setInvoiceId(1);
//        ii1b.setUnitPrice(product1b.getUnitCost());
//        ii1b.setInvoiceItemId(1);
//
//        ii2a.setInventoryId(2);
//        ii2a.setQuantity(5);
//        ii2a.setUnitPrice(product2b.getUnitCost());
//
//        ii2b.setInventoryId(2);
//        ii2b.setQuantity(5);
//        ii2b.setInvoiceId(2);
//        ii2b.setUnitPrice(product2b.getUnitCost());
//        ii2b.setInvoiceItemId(2);
//
//        ii2c.setInventoryId(2);
//        ii2c.setQuantity(8);
//        ii2c.setInvoiceId(2);
//        ii2c.setUnitPrice(product2c.getUnitCost());
//        ii2c.setInvoiceItemId(2);
//
//        // invoice item list
//        iiList1a = new ArrayList<>();
//        iiList1b = new ArrayList<>();
//        iiList2a = new ArrayList<>();
//        iiList2b = new ArrayList<>();
//        iiList2c = new ArrayList<>();
//
//        iiList1a.add(ii1a);
//        iiList1b.add(ii1b);
//        iiList2a.add(ii2a);
//        iiList2b.add(ii2b);
//        iiList2c.add(ii2c);
//        iiList2c.add(ii2c2);
//
//        // invoice
//        ivm1a = new InvoiceViewModel();
//        ivm1b = new InvoiceViewModel();
//        ivm2a = new InvoiceViewModel();
//        ivm2b = new InvoiceViewModel();
//        ivm2c = new InvoiceViewModel();
//
//        ivm1a.setCustomerId(1);
//        ivm1a.setPurchaseDate(LocalDate.now());
//        ivm1a.setInvoiceItems(iiList1a);
//
//        ivm1b.setCustomerId(1);
//        ivm1b.setInvoiceItems(iiList1b);
//        ivm1b.setPurchaseDate(LocalDate.now());
//        ivm1b.setInvoiceId(1);
//
//        ivm2a.setCustomerId(2);
//        ivm2a.setPurchaseDate(LocalDate.now());
//        ivm2a.setInvoiceItems(iiList2a);
//
//        ivm2b.setCustomerId(2);
//        ivm2b.setInvoiceItems(iiList2b);
//        ivm2b.setPurchaseDate(LocalDate.now());
//        ivm2b.setInvoiceId(2);
//
//        ivm2c.setCustomerId(2);
//        ivm2c.setInvoiceItems(iiList2c);
//        ivm2c.setPurchaseDate(LocalDate.now());
//        ivm2c.setInvoiceId(2);
//
//        // LEVEL UP
//        levelUp1a = new LevelUp();
//        levelUp1b = new LevelUp();
//        levelUp2a = new LevelUp();
//        levelUp2b = new LevelUp();
//        levelUp2c = new LevelUp();
//
//        levelUp1a.setCustomerId(1);
//        levelUp1a.setMemberDate(LocalDate.parse("2019-01-01"));
//        levelUp1a.setPoints(50);
//
//        levelUp1b.setCustomerId(1);
//        levelUp1b.setMemberDate(LocalDate.parse("2019-01-01"));
//        levelUp1b.setPoints(50);
//        levelUp1b.setLevelUpId(1);
//
//        levelUp2a.setCustomerId(2);
//        levelUp2a.setMemberDate(LocalDate.parse("2019-02-01"));
//        levelUp2a.setPoints(50);
//
//        levelUp2b.setCustomerId(2);
//        levelUp2b.setMemberDate(LocalDate.parse("2019-02-01"));
//        levelUp2b.setPoints(50);
//        levelUp2b.setLevelUpId(2);
//
//        levelUp2c.setCustomerId(2);
//        levelUp2c.setMemberDate(LocalDate.parse("2019-02-01"));
//        levelUp2c.setPoints(75);
//        levelUp2c.setLevelUpId(2);
//
//        // INVOICE ITEM VIEW MODEL
//        iivm1a = new InvoiceItemViewModel();
//        iivm1b = new InvoiceItemViewModel();
//        iivm2a = new InvoiceItemViewModel();
//        iivm2b = new InvoiceItemViewModel();
//        iivm2c = new InvoiceItemViewModel();
//        iivm2c2 = new InvoiceItemViewModel();
//
//        build(ii1a, product1a, iivm1a);
//        build(ii1b, product1b, iivm1b);
//        build(ii2a, product2a, iivm2a);
//        build(ii2b, product2b, iivm2b);
//        build(ii2c, product2c, iivm2c);
//        build(ii2c2, product2c2, iivm2c2);
//
//        iivmList1a = new ArrayList<>();
//        iivmList1b = new ArrayList<>();
//        iivmList2a = new ArrayList<>();
//        iivmList2b = new ArrayList<>();
//        iivmList2c = new ArrayList<>();
//
//        iivmList1a.add(iivm1a);
//        iivmList1b.add(iivm1b);
//        iivmList2a.add(iivm2a);
//        iivmList2b.add(iivm2b);
//        iivmList2c.add(iivm2c);
//        iivmList2c.add(iivm2c2);
//    }
//
//    public void destroySampleData() {
//        customer1a = null;
//        customer1b = null;
//        customer2a = null;
//        customer2b = null;
//        customer2c = null;
//
//        inventory1a = null;
//        inventory1b = null;
//        inventory1c = null;
//        inventory2a = null;
//        inventory2b = null;
//        inventory2c = null;
//        inventory2c2 = null;
//
//        ivm1a = null;
//        ivm1b = null;
//        ivm2a = null;
//        ivm2b = null;
//        ivm2c = null;
//
//        ii1a = null;
//        ii1b = null;
//        ii2a = null;
//        ii2b = null;
//        ii2c = null;
//        ii2a2 = null;
//        ii2b2 = null;
//        ii2c2 = null;
//
//        iiList1a = null;
//        iiList1b = null;
//        iiList2a = null;
//        iiList2b = null;
//        iiList2c = null;
//
//        levelUp1a = null;
//        levelUp1b = null;
//        levelUp2a = null;
//        levelUp2b = null;
//        levelUp2c = null;
//
//        product1a = null;
//        product1b = null;
//        product2a = null;
//        product2b = null;
//        product2c = null;
//        product2c2 = null;
//        product4 = null;
//
//        iivm1a = null;
//        iivm1b = null;
//        iivm2a = null;
//        iivm2b = null;
//        iivm2c = null;
//
//        iivmList1a = null;
//        iivmList1b = null;
//        iivmList2a = null;
//        iivmList2b = null;
//        iivmList2c = null;
//    }
//
//    public void build(InvoiceItem invoiceItem, Product product, InvoiceItemViewModel iivm) {
//        (new MapClasses<>(invoiceItem, iivm)).mapFirstToSecond(false);
//        iivm.setProduct(product);
//    }
//
//    @Test
//    public void getAllInventory() {
////        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
////                String.class)).contains("Hello World");
//
//    }
//
//    @Test
//    public void getInventoryByInventoryId() {
//    }
//
//    @Test
//    public void getProductByProductId() {
//    }
//
//    @Test
//    public void getAllProductsWithInventory() {
//    }
//
//    @Test
//    public void getCustomerByCustomerId() throws Exception {
//
////        String str = this.restTemplate.getForObject(protocolDomain + port + "/customers/1", String.class);
////
////        Customer customer = mapper.readValue(str, new TypeReference<Customer>(){});
////        assertThat(customer).isEqualTo(customer1b);
//
//        String expectedOutput = mapper.writeValueAsString(customer1b);
//
//        this.mockMvc.perform(get("/customers/{id}", 1))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedOutput));
//
//    }
//
//    @Test
//    public void getCustomerInvoices() {
//    }

    @Test
    public void createInvoice() {
    }
}