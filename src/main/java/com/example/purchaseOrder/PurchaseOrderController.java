package com.example.purchaseOrder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping("/list")
    public List<PurchaseOrder> listOrders(@RequestHeader String role) {
        switch (role.toUpperCase()) {
            case "PUBLISHER":
                return purchaseOrderService.getOrdersByStatus("created");
            case "REVIEWER":
                return purchaseOrderService.getOrdersByStatus("reworked");
            case "APPROVER":
                return purchaseOrderService.getOrdersByStatus("reviewed");
            default:
                throw new RuntimeException("Unauthorized role");
        }
    }

    @PostMapping("/txn")
    public PurchaseOrder createOrUpdateOrder(@RequestBody PurchaseOrder purchaseOrder) {
        return purchaseOrderService.createOrUpdateOrder(purchaseOrder);
    }

    @PatchMapping("/txn")
    public PurchaseOrder handleAction(@RequestBody PurchaseOrder purchaseOrder, @RequestHeader String role) {
        PurchaseOrder existingOrder = purchaseOrderService.getOrderById(purchaseOrder.getRecordId());

        switch (role.toUpperCase()) {
            case "REVIEWER":
            case "APPROVER":
                existingOrder.setStatus(purchaseOrder.getStatus());
                existingOrder.getReviewSummary().addAll(purchaseOrder.getReviewSummary());
                existingOrder.setUpdatedBy(role);
                break;
            default:
                throw new RuntimeException("Unauthorized role");
        }

        return purchaseOrderService.createOrUpdateOrder(existingOrder);
    }
}
