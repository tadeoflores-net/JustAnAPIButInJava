package com.example.legacy.support;
import java.time.LocalDateTime;

public class DeadLetterItem {

    private String operation;
    private String error;
    private LocalDateTime createdAt = LocalDateTime.now();

    public DeadLetterItem(String operation, String error) {
        this.operation = operation;
        this.error = error;
    }

    public String getOperationId() {
        return operation;
    }

    public String getError() {
        return error;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
