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
import java.util.Comparator;
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
        product.setUnitCost(ivm.getUnitCost());
        product = productClient.createProduct(product);
        /** Creating inventory */
        Inventory inventory = new Inventory();
        inventory.setProductId(product.getProductId());
        if (ivm.getQuantityInInventory()==null) inventory.setQuantity(0);
        else inventory.setQuantity(ivm.getQuantityInInventory());
        inventory = inventoryClient.createInventory(inventory);
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
        if(product==null) return null;
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
        if (product==null) return null;
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
        if (ivm.getInventoryId()!=null) {
            Inventory inventory = inventoryClient.findInventoryByInventoryId(ivm.getInventoryId());
            inventory.setQuantity(ivm.getQuantityInInventory());
            inventoryClient.updateInventory(inventory);
        }
    }

    public void deleteItem(Integer productId)
    {
        List<Inventory> invList = inventoryClient.findInventoriesByProductId(productId);
        if(invList!=null)
        {
            invList.stream().forEach(inventory -> {
                inventoryClient.deleteInventory(inventory.getInventoryId());
            });
        }
        productClient.deleteProduct(productId);
    }

    /** Helper Method - building the ItemViewModel */
    public ItemViewModel buildItemViewModel(Product product)
    {
        if (product==null) return null;
        ItemViewModel ivm = new ItemViewModel();
        ivm.setProductId(product.getProductId());
        ivm.setProductName(product.getProductName());
        ivm.setProductDescription(product.getProductDescription());
        ivm.setListPrice(product.getListPrice());
        ivm.setUnitCost(product.getUnitCost());
        List<Inventory> inventoryList = inventoryClient.findInventoriesByProductId(product.getProductId());
        if (inventoryList.size() > 0) {
            Inventory inventory = deleteExtraInventories(inventoryList);
            ivm.setInventoryId(inventory.getInventoryId());
            ivm.setQuantityInInventory(inventory.getQuantity());
        }
        return ivm;
    }

    /** Deleting extra Inventories */
    public Inventory deleteExtraInventories(List<Inventory> inventoryList)
    {
        Comparator<Inventory> maxId = Comparator.comparing(Inventory::getInventoryId);
        Inventory maxIdInv = inventoryList.stream().max(maxId).get();
        for (Inventory inventory: inventoryList)
        {
            if(inventory.getInventoryId()!=maxIdInv.getInventoryId())
            {
                maxIdInv.setQuantity(maxIdInv.getQuantity()+inventory.getQuantity());
                inventoryClient.deleteInventory(inventory.getInventoryId());
            }
        }
        inventoryClient.updateInventory(maxIdInv);
        return maxIdInv;
    }
}
