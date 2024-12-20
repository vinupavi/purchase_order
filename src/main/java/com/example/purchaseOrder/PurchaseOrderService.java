package com.example.purchaseOrder;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public List<PurchaseOrder> getOrdersByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    public PurchaseOrder createOrUpdateOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder getOrderById(String recordId) {
        return purchaseOrderRepository.findById(recordId).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}