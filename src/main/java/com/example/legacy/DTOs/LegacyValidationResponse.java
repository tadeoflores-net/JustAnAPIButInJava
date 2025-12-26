package com.example.legacy.DTOs;

public class LegacyValidationResponse {

    private String status;
    private String message;

    public LegacyValidationResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
