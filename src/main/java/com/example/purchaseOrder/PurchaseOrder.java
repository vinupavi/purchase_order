package com.example.purchaseOrder;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Id;

@Entity
public class PurchaseOrder {

    @Id
    private String recordId;

    private String taskId;
    private double partsPrice;
    private double labourPrice;
    private double amount;
    private String status;

    @ElementCollection
    private List<String> reviewSummary = new ArrayList<>();

    private String createdBy;
    private String updatedBy;

    // Getters and Setters
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public double getPartsPrice() {
        return partsPrice;
    }

    public void setPartsPrice(double partsPrice) {
        this.partsPrice = partsPrice;
    }

    public double getLabourPrice() {
        return labourPrice;
    }

    public void setLabourPrice(double labourPrice) {
        this.labourPrice = labourPrice;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getReviewSummary() {
        return reviewSummary;
    }

    public void setReviewSummary(List<String> reviewSummary) {
        this.reviewSummary = reviewSummary;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}