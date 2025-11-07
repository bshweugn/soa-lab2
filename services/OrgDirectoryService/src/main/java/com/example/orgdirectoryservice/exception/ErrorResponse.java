package com.example.orgdirectoryservice.exception;

import java.util.List;

public class ErrorResponse {
    private int code;
    private String message;
    private List<String> details;

    public ErrorResponse() {}

    public ErrorResponse(int code, String message, List<String> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }


    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }


}
