package com.trilogyed.adminapi.service;

import com.trilogyed.adminapi.domain.ItemViewModel;
import com.trilogyed.adminapi.model.Inventory;
import com.trilogyed.adminapi.model.Product;
import com.trilogyed.adminapi.util.feign.InventoryServiceClient;
import com.trilogyed.adminapi.util.feign.ProductServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductServiceLayer {
    private ProductServiceClient productClient;
    private InventoryServiceClient inventoryClient;

    @Autowired

    public ProductServiceLayer(ProductServiceClient productClient, InventoryServiceClient inventoryClient) {
        this.productClient = productClient;
        this.inventoryClient = inventoryClient;
    }

    @Transactional
    public ItemViewModel createItem(ItemViewModel ivm)
    {
        Product product = new Product();
        product.setProductName(ivm.getProductName());
        product.setProductDescription(ivm.getProductDescription());
        product.setListPrice(ivm.getListPrice());
        product.setListPrice(ivm.getUnitCost());
        product = productClient.createProduct(product);
        if (ivm.getInventoryList().size()==0) {
            Inventory inventory = new Inventory();
            inventory.setProductId(product.getProductId());
            inventory.setQuantity(0);
            inventory = inventoryClient.createInventory(inventory);
        }else
        {
            List<Inventory> ivmList = ivm.getInventoryList();
            Product finalProduct = product;
            ivmList.stream().forEach(inventory ->
            {
                inventory.setProductId(finalProduct.getProductId());
                inventory = inventoryClient.createInventory(inventory);
            });
        }
        return buildItemViewModel(product);
    }

    @Transactional
    public ItemViewModel createInventory(Inventory inventory)
    {
        inventory = inventoryClient.createInventory(inventory);
        Product product = productClient.findProductByProductId(inventory.getProductId());
        return buildItemViewModel(product);
    }

    public ItemViewModel findItemByProductId(Integer productId)
    {
        Product product = productClient.findProductByProductId(productId);
        return buildItemViewModel(product);
    }

    public List<ItemViewModel> findAllItems()
    {
        List<Product> products = productClient.findAllProducts();
        List<ItemViewModel> ivmList = new ArrayList<>();
        products.stream().forEach(product ->{
            ItemViewModel ivm = buildItemViewModel(product);
            ivmList.add(ivm);
        });
        return ivmList;
    }

    public ItemViewModel findItemByInventoryId(Integer inventoryId)
    {
        Inventory inventory = inventoryClient.findInventoryByInventoryId(inventoryId);
        Product product = productClient.findProductByProductId(inventory.getProductId());
        return buildItemViewModel(product);
    }

    public void updateItemOrInventory(ItemViewModel ivm)
    {
        Product product = productClient.findProductByProductId(ivm.getProductId());
        product.setProductName(ivm.getProductName());
        product.setProductDescription(ivm.getProductDescription());
        product.setListPrice(ivm.getListPrice());
        product.setUnitCost(ivm.getUnitCost());
        productClient.updateProduct(product);
        updateInventory(ivm);
    }

    public void updateInventory(ItemViewModel ivm)
    {
        List<Inventory> updatedInventoryList = ivm.getInventoryList();
        if (ivm.getInventoryList().size()!=0) {
            updatedInventoryList.stream().forEach(inventory -> {
                Inventory inventory1 = inventoryClient.findInventoryByInventoryId(inventory.getInventoryId());
                inventory1.setQuantity(inventory.getQuantity());
                inventoryClient.updateInventory(inventory);
            });
        }
    }

    public void deleteItem(Integer productId)
    {
        List<Inventory> invList = inventoryClient.findInventoriesByProductId(productId);
        invList.stream().forEach(inventory -> {
            inventoryClient.deleteInventory(inventory.getInventoryId());
        });
        productClient.deleteProduct(productId);
    }

    public void deleteInventory(Integer inventoryId)
    {
        inventoryClient.deleteInventory(inventoryId);
    }

    /** Helper Method - building the ItemViewModel */
    public ItemViewModel buildItemViewModel(Product product)
    {
        ItemViewModel ivm = new ItemViewModel();
        ivm.setProductId(product.getProductId());
        ivm.setProductName(product.getProductName());
        ivm.setProductDescription(product.getProductDescription());
        ivm.setListPrice(product.getListPrice());
        ivm.setUnitCost(product.getUnitCost());
        ivm.setInventoryList(inventoryClient.findInventoriesByProductId(product.getProductId()));
        return ivm;
    }
}
