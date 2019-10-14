package com.trilogyed.retailapiservice.util.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.trilogyed.retailapiservice.domain.Inventory;
import com.trilogyed.retailapiservice.exception.InventoryServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryRestService {
    /*
    inventoryServiceName=U2-INVENTORY-SERVICE
inventoryServiceProtocol=http://
inventoryServiceFindAllInventories=/inventory
     */

    @Autowired
    DiscoveryClient discoveryClient;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${inventoryServiceName}")
    private String inventoryServiceName;

    @Value("${inventoryServiceProtocol}")
    private String inventoryServiceProtocol;

    @Value("${inventoryServiceFindAllInventories}")
    private String inventoryServiceFindAllInventories;

    @HystrixCommand(fallbackMethod = "fallbackList")
    public List<Inventory> findAllInventories() {
        List<ServiceInstance> instances = discoveryClient.getInstances(inventoryServiceName);
        String uri = inventoryServiceProtocol + instances.get(0).getHost() + ":" +
                instances.get(0).getPort() + inventoryServiceFindAllInventories;
        List<Inventory> inventories = new ArrayList<>();
        ResponseEntity<List<Inventory>> response = restTemplate.exchange(
                uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Inventory>>(){});
        inventories = response.getBody();
        return inventories;
    }

    private List<Inventory> fallbackList() {
        throw new InventoryServiceUnavailableException("Unable to fetch available inventory at the moment.");
    }
}
