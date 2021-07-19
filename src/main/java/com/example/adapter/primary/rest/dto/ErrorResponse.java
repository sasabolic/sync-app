package com.example.adapter.primary.rest.dto;

/**
 * Error response DTO.
 *
 * @author Saša Bolić
 */
public class ErrorResponse {
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
