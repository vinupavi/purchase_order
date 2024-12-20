package com.example.purchaseOrder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkflowApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Mock
    private PurchaseOrderService purchaseOrderService;

    @InjectMocks
    private WorkflowApplicationTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListOrdersAsPublisher() {
        // Arrange
        List<PurchaseOrder> mockOrders = new ArrayList<>();
        PurchaseOrder order = new PurchaseOrder();
        order.setRecordId("03457:123456");
        order.setStatus("created");
        mockOrders.add(order);

        when(purchaseOrderService.getOrdersByStatus("created")).thenReturn(mockOrders);

        // Act
        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/orders/list", List.class, "PUBLISHER");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(purchaseOrderService, times(1)).getOrdersByStatus("created");
    }

    @Test
    public void testCreateOrder() {
        // Arrange
        PurchaseOrder order = new PurchaseOrder();
        order.setRecordId("03457:123456");
        order.setTaskId("replace pump");
        order.setPartsPrice(150);
        order.setLabourPrice(50);
        order.setAmount(200);
        order.setStatus("created");
        order.setCreatedBy("UserX");

        when(purchaseOrderService.createOrUpdateOrder(any(PurchaseOrder.class))).thenReturn(order);

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.add("role", "PUBLISHER");
        ResponseEntity<PurchaseOrder> response = restTemplate.postForEntity("http://localhost:" + port + "/orders/txn", order, PurchaseOrder.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("created", response.getBody().getStatus());
        verify(purchaseOrderService, times(1)).createOrUpdateOrder(any(PurchaseOrder.class));
    }

    @Test
    public void testApproveOrder() {
        // Arrange
        PurchaseOrder existingOrder = new PurchaseOrder();
        existingOrder.setRecordId("03457:123456");
        existingOrder.setStatus("reviewed");

        when(purchaseOrderService.getOrderById("03457:123456")).thenReturn(existingOrder);
        when(purchaseOrderService.createOrUpdateOrder(any(PurchaseOrder.class))).thenReturn(existingOrder);

        PurchaseOrder updateOrder = new PurchaseOrder();
        updateOrder.setRecordId("03457:123456");
        updateOrder.setStatus("approved");
        updateOrder.setUpdatedBy("APPROVER");

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.add("role", "APPROVER");
        ResponseEntity<PurchaseOrder> response = restTemplate.patchForObject("http://localhost:" + port + "/orders/txn", updateOrder, PurchaseOrder.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("approved", response.getBody().getStatus());
        verify(purchaseOrderService, times(1)).getOrderById("03457:123456");
        verify(purchaseOrderService, times(1)).createOrUpdateOrder(any(PurchaseOrder.class));
    }
}
