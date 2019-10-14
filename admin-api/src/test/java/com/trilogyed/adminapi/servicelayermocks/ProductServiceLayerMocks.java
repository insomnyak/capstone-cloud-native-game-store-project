package com.trilogyed.adminapi.servicelayermocks;

import com.trilogyed.adminapi.domain.ItemViewModel;
import com.trilogyed.adminapi.model.Inventory;
import com.trilogyed.adminapi.model.Product;
import com.trilogyed.adminapi.service.ProductServiceLayer;
import com.trilogyed.adminapi.util.feign.InventoryServiceClient;
import com.trilogyed.adminapi.util.feign.ProductServiceClient;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ProductServiceLayerMocks {
    private ProductServiceClient productClient;
    private InventoryServiceClient inventoryClient;
    private ProductServiceLayer sl;

    @Test
    public void addGetUpdateDeleteProduct() {
        productClient = mock(ProductServiceClient.class);
        inventoryClient = mock(InventoryServiceClient.class);

        Product product = new Product();
        product.setProductName("Lamy");
        product.setProductDescription("Fountain Pen");
        product.setListPrice(new BigDecimal("2.49"));
        product.setUnitCost(new BigDecimal("1.49"));

        Product savedProduct = new Product();
        savedProduct.setProductId(100);
        savedProduct.setProductName("Lamy");
        savedProduct.setProductDescription("Fountain Pen");
        savedProduct.setListPrice(new BigDecimal("2.49"));
        savedProduct.setUnitCost(new BigDecimal("1.49"));

        Product anotherProduct = new Product();
        anotherProduct.setProductId(200);
        anotherProduct.setProductName("Lamy Elite");
        anotherProduct.setProductDescription("Fountain Pen");
        anotherProduct.setListPrice(new BigDecimal("2.49"));
        anotherProduct.setUnitCost(new BigDecimal("1.49"));

        List<Product> productList = new ArrayList<>();
        productList.add(savedProduct);
        productList.add(anotherProduct);

        Product updateProduct = new Product();
        updateProduct.setProductId(300);
        updateProduct.setProductName("Lamy");
        updateProduct.setProductDescription("Fountain Pen");
        updateProduct.setListPrice(new BigDecimal("2.49"));
        updateProduct.setUnitCost(new BigDecimal("1.49"));

        Product deleteProduct = new Product();
        deleteProduct.setProductId(400);
        deleteProduct.setProductName("Lamy");
        deleteProduct.setProductDescription("Fountain Pen");
        deleteProduct.setListPrice(new BigDecimal("2.49"));
        deleteProduct.setUnitCost(new BigDecimal("1.49"));

        Inventory inventory = new Inventory();
        inventory.setProductId(100);
        inventory.setQuantity(250);

        Inventory savedInventory = new Inventory();
        savedInventory.setInventoryId(1100);
        savedInventory.setProductId(100);
        savedInventory.setQuantity(250);

        Inventory anotherInventory = new Inventory();
        anotherInventory.setInventoryId(2200);
        anotherInventory.setProductId(200);
        anotherInventory.setQuantity(300);

        List<Inventory> invList = new ArrayList<>();
        invList.add(savedInventory);
        invList.add(anotherInventory);

        List<Inventory> invListByProductId0 = new ArrayList<>();
        invListByProductId0.add(savedInventory);

        List<Inventory> invListByProductId = new ArrayList<>();
        invListByProductId.add(anotherInventory);

        Inventory updateInventory = new Inventory();
        updateInventory.setInventoryId(3300);
        updateInventory.setProductId(300);
        updateInventory.setQuantity(250);

        List<Inventory> invListByProductId1 = new ArrayList<>();
        invListByProductId1.add(updateInventory);

        Inventory deleteInventory = new Inventory();
        deleteInventory.setInventoryId(4400);
        deleteInventory.setProductId(400);
        deleteInventory.setQuantity(250);

        doReturn(savedProduct).when(productClient).createProduct(product);
        doReturn(savedProduct).when(productClient).findProductByProductId(100);
        doReturn(productList).when(productClient).findAllProducts();
        doReturn(anotherProduct).when(productClient).findProductByProductId(200);
        doNothing().when(productClient).updateProduct(updateProduct);
        doReturn(updateProduct).when(productClient).findProductByProductId(300);
        doNothing().when(productClient).deleteProduct(400);
        doReturn(null).when(productClient).findProductByProductId(400);

        doReturn(savedInventory).when(inventoryClient).createInventory(inventory);
        doReturn(savedInventory).when(inventoryClient).findInventoryByInventoryId(1100);
        doReturn(invListByProductId0).when(inventoryClient).findInventoriesByProductId(100);
        doReturn(invList).when(inventoryClient).findAllInventories();
        doReturn(invListByProductId).when(inventoryClient).findInventoriesByProductId(200);
        doReturn(anotherInventory).when(inventoryClient).findInventoryByInventoryId(2200);
        doNothing().when(inventoryClient).updateInventory(updateInventory);
        doReturn(updateInventory).when(inventoryClient).findInventoryByInventoryId(3300);
        doReturn(invListByProductId1).when(inventoryClient).findInventoriesByProductId(300);
        doNothing().when(inventoryClient).deleteInventory(4400);
        doReturn(null).when(inventoryClient).findInventoriesByProductId(400);
        doReturn(null).when(inventoryClient).findInventoryByInventoryId(4400);

        sl = new ProductServiceLayer(productClient, inventoryClient);

        /** testing add ing a new product with inventory */
        ItemViewModel ivm = new ItemViewModel();
        ivm.setProductName(product.getProductName());
        ivm.setProductDescription(product.getProductDescription());
        ivm.setListPrice(product.getListPrice());
        ivm.setUnitCost(product.getUnitCost());
        ivm.setQuantityInInventory(inventory.getQuantity());

        ivm = sl.createItem(ivm);
        ItemViewModel ivm1 = sl.findItemByProductId(100);
        assertEquals(ivm, ivm1);

        /** testing creating inventory */
        ItemViewModel ivmCreate = sl.createInventory(inventory);
        ItemViewModel ivmGetBack = sl.findItemByProductId(ivmCreate.getProductId());
        assertEquals(ivmCreate, ivmGetBack);

        /** Testing find all Items */
        List<ItemViewModel> ivmListAllProducts = sl.findAllItems();
        assertEquals(ivmListAllProducts.size(), 2);

        /** Testing find Item By Inventory Id */
        ItemViewModel ivmFindItem = sl.findItemByInventoryId(1100);
        ItemViewModel ivmFindByProductId = sl.findItemByProductId(100);
        assertEquals(ivmFindItem, ivmFindByProductId);

        /** Testing update Item Or Inventory */
        ItemViewModel updateIvm = sl.findItemByProductId(300);
        sl.updateInventory(updateIvm);
        ItemViewModel updatedIvm = sl.findItemByProductId(updateIvm.getProductId());
        assertEquals(updatedIvm, updateIvm);
        assertEquals(updatedIvm.getQuantityInInventory(), updateIvm.getQuantityInInventory());

        /** Testing delete Item */
        sl.deleteItem(400);
        assertNull(sl.findItemByProductId(400));
    }
}
